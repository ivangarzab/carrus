package com.ivangarzab.carrus.ui.overview

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Message
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.MessageQueueRepository
import com.ivangarzab.carrus.data.structures.LiveState
import com.ivangarzab.carrus.data.structures.asUniqueMessageQueue
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val messageQueueRepository: MessageQueueRepository
) : ViewModel() {

    val state: LiveState<OverviewState> = LiveState(OverviewState())

    val queueState: LiveState<MessageQueueState> = LiveState(MessageQueueState())

    val triggerNotificationPermissionRequest: LiveEvent<Boolean> = LiveEvent()
    val triggerAlarmsPermissionRequest: LiveEvent<Boolean> = LiveEvent()

    init {
        viewModelScope.launch {
            carRepository.observeCarData()
                .catch { Timber.d("Something went wrong collecting the car data") }
                .collect {
                    Timber.d("Got a car update from the repository: $it")
                    updateCarState(it)
                }
        }
        viewModelScope.launch {
            appSettingsRepository.observeAppSettingsStateData().collect {
                state.setState { copy(dueDateFormat = it.dueDateFormat) }
            }
        }
        viewModelScope.launch {
            messageQueueRepository.observeMessageQueueFlow().collect {
                queueState.setState {
                    //TODO: Fix the cast
                    copy(messageQueue = it.asUniqueMessageQueue())
                }
            }
        }
    }

    fun processStateChange(
        state: OverviewState,
        areNotificationsEnabled: Boolean
    ) {
        Timber.v("Processing overview state change")
        state.car?.let {
            processCarServicesListForNotification(
                services = it.services,
                areNotificationsEnabled = areNotificationsEnabled
            )
        }
    }

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        carRepository.removeCarService(service)
    }

    fun onNotificationPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) {
            removeNotificationPermissionMessage()
        }
    }

    fun onMessageClicked(id: String) {
        Timber.d("Got a message click with id=$id")
        when (id) {
            Message.MISSING_PERMISSION_NOTIFICATION.data.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    triggerNotificationPermissionRequest.postValue(true)
                }
                removeNotificationPermissionMessage()
            }

            Message.MISSING_PERMISSION_ALARM.data.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    triggerAlarmsPermissionRequest.postValue(true)
                }
                removeAlarmPermissionMessage()
            }

            Message.TEST.data.id -> removeTestMessage()
        }
    }

    fun onMessageDismissed() {
        Timber.v("Removing message at the top of the queue")
        messageQueueRepository.dismissMessage()
    }

    private fun addNotificationPermissionMessage() = with(Message.MISSING_PERMISSION_NOTIFICATION) {
        Timber.v("Adding ${this.name} message to the queue")
        messageQueueRepository.addMessage(this)
        state.setState {
            copy(hasPromptedForPermissionNotification = true)
        }
    }

    private fun removeNotificationPermissionMessage() =
        with(Message.MISSING_PERMISSION_NOTIFICATION) {
            Timber.v("Removing ${this.name} message from queue")
            messageQueueRepository.removeMessage(this)
        }

    fun checkForAlarmPermission(canScheduleExactAlarms: Boolean) {
        val hasPromptedForPermissionAlarm = state.value?.hasPromptedForPermissionAlarm ?: false
        if (canScheduleExactAlarms.not() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            hasPromptedForPermissionAlarm.not()
        ) {
            addAlarmPermissionMessage()
            Timber.d("Registering alarm permission state changed broadcast receiver")
        }
    }

    private fun addAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        Timber.v("Adding ${this.name} message to the queue")
        messageQueueRepository.addMessage(this)
        state.setState {
            copy(hasPromptedForPermissionAlarm = true)
        }
    }

    private fun removeAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        Timber.v("Removing ${this.name} message from queue")
        messageQueueRepository.removeMessage(this)
    }

    fun addTestMessage() = messageQueueRepository.addMessage(Message.TEST)
    private fun removeTestMessage() = messageQueueRepository.removeMessage(Message.TEST)

    private fun onSortingByType(type: SortingCallback.SortingType) {
        when (type) {
            SortingCallback.SortingType.NONE -> resetServicesSort()
            SortingCallback.SortingType.NAME -> sortServicesByName()
            SortingCallback.SortingType.DATE -> sortServicesByDate()
        }
        state.setState {
            copy(serviceSortingType = type)
        }
    }

    private fun resetServicesSort() {
        Timber.v("Resetting services sorting")
        updateCarState(carRepository.fetchCarData())
    }

    private fun sortServicesByName() {
        Timber.v("Sorting services by name")
        state.value?.car?.let { car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.name }
                )
            )
        }
    }

    private fun sortServicesByDate() {
        Timber.v("Sorting services by due date")
        state.value?.car?.let { car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.dueDate }
                )
            )
        }
    }

    private fun processCarServicesListForNotification(
        services: List<Service>,
        areNotificationsEnabled: Boolean
    ) {
        if (services.isNotEmpty()) {
            when (areNotificationsEnabled) {
                true -> {
                    if (alarmsRepository.isPastDueAlarmActive().not()) {
                        alarmsRepository.schedulePastDueAlarm()
                    } else {
                        Timber.v("'Past Due' alarm is already scheduled")
                    }
                }

                false -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        state.value?.hasPromptedForPermissionNotification?.not() == true
                    ) {
                        addNotificationPermissionMessage()
                    } else {
                        Timber.v("We don't need Notification permission for sdk=${Build.VERSION.SDK_INT} (<33)")
                    }
                }
            }
        }
    }

    private fun updateCarState(car: Car?) = state.setState { copy(car = car) }

    fun onSort(type: SortingCallback.SortingType) {
        Timber.v("Got a sorting request with type=$type")
        onSortingByType(type)
    }

    fun setupEasterEggForTesting() {
        if (App.isRelease().not()) {
            state.value?.car?.let {
                carRepository.saveCarData(
                    it.copy(
                        services = Service.serviceList
                    )
                )
            }
        }
    }
}