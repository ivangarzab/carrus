package com.ivangarzab.carrus.ui.overview

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.Message
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.MessageQueueRepository
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.util.extensions.setState
import com.ivangarzab.carrus.util.managers.UniqueMessageQueue
import com.ivangarzab.carrus.util.managers.asUniqueMessageQueue
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
    private val savedState: SavedStateHandle,
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val messageQueueRepository: MessageQueueRepository
    ) : ViewModel(), SortingCallback {

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
    )

    val queueState: LiveData<MessageQueueState> = savedState.getLiveData(
        QUEUE_STATE,
        MessageQueueState()
    )

    private val _nightThemeState: MutableLiveData<Boolean> = MutableLiveData(false)
    val nightThemeState: LiveData<Boolean> = _nightThemeState

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
            appSettingsRepository.observeDueDateFormatData().collect {
                setState(state, savedState, STATE) { copy(dueDateFormat = it) }
            }
        }
        viewModelScope.launch {
            appSettingsRepository.observeNightThemeData().collect {
                _nightThemeState.value = it
            }
        }
        viewModelScope.launch {
            messageQueueRepository.observeMessageQueueFlow().collect {
                updateQueueState(it.asUniqueMessageQueue())
            }
        }
    }

    fun getDueDateFormat(): DueDateFormat = appSettingsRepository.fetchDueDateFormatSetting()

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        carRepository.removeCarService(service)
    }

    fun onNotificationPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) { removeNotificationPermissionMessage() }
    }

    fun onMessageDismissed() {
        Timber.v("Removing message at the top of the queue")
        messageQueueRepository.dismissMessage()
    }

    fun addNotificationPermissionMessage() = with(Message.MISSING_PERMISSION_NOTIFICATION) {
        Timber.v("Adding ${this.name} message to the queue")
        messageQueueRepository.addMessage(this)
        setState(state, savedState, STATE) {
            copy(hasPromptedForPermissionNotification = true)
        }
    }

    private fun removeNotificationPermissionMessage() = with(Message.MISSING_PERMISSION_NOTIFICATION) {
        Timber.v("Removing ${this.name} message from queue")
        messageQueueRepository.removeMessage(this)
    }

    fun addAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        Timber.v("Adding ${this.name} message to the queue")
        messageQueueRepository.addMessage(this)
        setState(state, savedState, STATE) {
            copy(hasPromptedForPermissionAlarm = true)
        }
    }

    fun removeAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        Timber.v("Removing ${this.name} message from queue")
        messageQueueRepository.removeMessage(this)
    }

    fun addTestMessage() = messageQueueRepository.addMessage(Message.TEST)

    private fun onSortingByType(type: SortingCallback.SortingType) {
        when (type) {
            SortingCallback.SortingType.NONE -> resetServicesSort()
            SortingCallback.SortingType.NAME -> sortServicesByName()
            SortingCallback.SortingType.DATE -> sortServicesByDate()
        }
        setState(state, savedState, STATE) {
            copy(serviceSortingType = type)
        }
    }

    private fun resetServicesSort() {
        Timber.v("Resetting services sorting")
        updateCarState(carRepository.fetchCarData())
    }

    private fun sortServicesByName() {
        Timber.v("Sorting services by name")
        state.value?.car?.let {car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.name }
                )
            )
        }
    }

    private fun sortServicesByDate() {
        Timber.v("Sorting services by due date")
        state.value?.car?.let {car ->
            updateCarState(
                car.copy(
                    services = car.services.sortedBy { it.dueDate }
                )
            )
        }
    }

    fun processCarServicesListForNotification(
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

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    private fun updateQueueState(queue: UniqueMessageQueue) {
        setState(queueState, savedState, QUEUE_STATE) { copy(messageQueue = queue)}
    }

    fun isNight(): Boolean = appSettingsRepository.fetchNightThemeSetting() ?: false

    override fun onSort(type: SortingCallback.SortingType) {
        Timber.v("Got a sorting request with type=$type")
        onSortingByType(type)
    }

    fun setupEasterEggForTesting() {
        state.value?.car?.let {
            carRepository.saveCarData(it.copy(
                services = Service.serviceList
            ))
        }
    }

    companion object {
        private const val STATE: String = "OverviewViewModel.STATE"
        private const val QUEUE_STATE: String = "OverviewViewModel.QUEUE_STATE"
    }
}