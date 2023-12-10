package com.ivangarzab.carrus.ui.overview

import android.os.Build
import androidx.annotation.VisibleForTesting
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
import com.ivangarzab.carrus.ui.overview.data.SortingType
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.carrus.util.providers.BuildVersionProvider
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
    private val buildVersionProvider: BuildVersionProvider,
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val messageQueueRepository: MessageQueueRepository,
    private val analytics: Analytics
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

    //TODO: Break this function down into more digestible and manageable pieces
    fun processStateChange(
        state: OverviewState,
        areNotificationsEnabled: Boolean
    ) {
        state.car?.let {
            Timber.v("Processing overview state change")
            if (it.services.isNotEmpty()) {
                when (areNotificationsEnabled) {
                    true -> {
                        if (alarmsRepository.isPastDueAlarmActive().not()) {
                            alarmsRepository.schedulePastDueAlarm()
                        } else {
                            Timber.v("'Past Due' alarm is already scheduled")
                        }
                    }

                    false -> {
                        if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.TIRAMISU &&
                            this.state.value?.hasPromptedForPermissionNotification?.not() == true
                        ) {
                            addNotificationPermissionMessage()
                        } else {
                            Timber.v("We don't need Notification permission for sdk=${buildVersionProvider.getSdkVersionInt()} (<33)")
                        }
                    }
                }
            }
        }
    }

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        carRepository.removeCarService(service)
        analytics.logServiceDeleted(service.id, service.name)
    }

    fun onNotificationPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) {
            removeNotificationPermissionMessage()
        }
        analytics.logNotificationPermissionResult(isGranted)
    }

    fun onMessageClicked(id: String) {
        Timber.d("Got a message click with id=$id")
        analytics.logAppMessageClicked(id)
        when (id) {
            Message.MISSING_PERMISSION_NOTIFICATION.data.id -> {
                if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.TIRAMISU) {
                    triggerNotificationPermissionRequest.postValue(true)
                }
                removeNotificationPermissionMessage()
            }

            Message.MISSING_PERMISSION_ALARM.data.id -> {
                if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.S) {
                    triggerAlarmsPermissionRequest.postValue(true)
                }
                removeAlarmPermissionMessage()
            }

            Message.TEST.data.id -> removeMessage(Message.TEST)
        }
    }

    @VisibleForTesting
    fun addNotificationPermissionMessage() = with(Message.MISSING_PERMISSION_NOTIFICATION) {
        addMessage(this)
        state.setState {
            copy(hasPromptedForPermissionNotification = true)
        }
    }

    private fun removeNotificationPermissionMessage() =
        with(Message.MISSING_PERMISSION_NOTIFICATION) {
            removeMessage(this)
        }

    fun checkForAlarmPermission(canScheduleExactAlarms: Boolean) {
        val hasPromptedForPermissionAlarm = state.value?.hasPromptedForPermissionAlarm ?: false
        if (canScheduleExactAlarms.not() &&
            hasPromptedForPermissionAlarm.not() &&
            buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.S
        ) {
            addAlarmPermissionMessage()
            Timber.d("Registering alarm permission state changed broadcast receiver")
        }
    }

    @VisibleForTesting
    fun addAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        addMessage(this)
        state.setState {
            copy(hasPromptedForPermissionAlarm = true)
        }
    }

    private fun removeAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        removeMessage(this)
    }

    fun addTestMessage() {
        if (App.isRelease().not()) {
            addMessage(Message.TEST)
        }
    }

    private fun addMessage(type: Message) = with(type) {
        Timber.v("Adding ${this.name} message to the queue")
        analytics.logAppMessageAdded(this.name)
        messageQueueRepository.addMessage(this)
    }

    private fun removeMessage(type: Message) = with(type) {
        Timber.v("Removing ${this.name} message from queue")
        analytics.logAppMessageRemoved(this.name)
        messageQueueRepository.removeMessage(this)
    }

    fun onMessageDismissed() {
        Timber.v("Removing message at the top of the queue")
        //TODO: How do we instrument this, or should we simply use removeMessage instead?
        messageQueueRepository.dismissMessage()
    }

    private fun onSortingByType(type: SortingType) {
        when (type) {
            SortingType.NONE -> resetServicesSort()
            SortingType.NAME -> sortServicesByName()
            SortingType.DUE_DATE -> sortServicesByDueDate()
            SortingType.REPAIR_DATE -> sortServicesByRepairDate()
        }
        analytics.logServiceListSorted(type.name)
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

    private fun sortServicesByDueDate() {
        Timber.v("Sorting services by due date")
        state.value?.car?.let { car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.dueDate }
                )
            )
        }
    }

    private fun sortServicesByRepairDate() {
        Timber.v("Sorting services by repair date")
        state.value?.car?.let { car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.repairDate }
                )
            )
        }
    }

    private fun updateCarState(car: Car?) = state.setState { copy(car = car) }

    fun onSort(type: SortingType) {
        Timber.v("Got a sorting request with type=$type")
        analytics.logSortClicked(type.name)
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