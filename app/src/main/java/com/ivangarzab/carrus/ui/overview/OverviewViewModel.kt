package com.ivangarzab.carrus.ui.overview

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carrus.alarms
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.Message
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.serviceList
import com.ivangarzab.carrus.util.extensions.setState
import com.ivangarzab.carrus.util.managers.UniqueMessageQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository
    ) : ViewModel() {

    @Parcelize
    data class OverviewState(
        val car: Car? = null,
        val serviceSortingType: SortingCallback.SortingType = SortingCallback.SortingType.NONE,
        val hasPromptedForPermissionNotification: Boolean = false,
        val hasPromptedForPermissionAlarm: Boolean = false
    ) : Parcelable

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
    )

    @Parcelize
    data class QueueState(
        val messageQueue: UniqueMessageQueue = UniqueMessageQueue()
    ) : Parcelable

    val queueState: LiveData<QueueState> = savedState.getLiveData(
        QUEUE_STATE,
        QueueState()
    )

    private val _nightThemeState: MutableLiveData<Boolean> = MutableLiveData(false)
    val nightThemeState: LiveData<Boolean> = _nightThemeState

    /** Pair<repairDate, dueDate> */
    var datesInMillis: Pair<Long, Long> = Pair(0, 0)

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
            appSettingsRepository.observeNightThemeData().collect {
                _nightThemeState.value = it
            }
        }
    }

    fun verifyServiceData(name: String): Boolean =
        name.isNotBlank() &&
                datesInMillis.first != 0L &&
                datesInMillis.second != 0L

    fun onServiceCreated(service: Service) {
        Timber.d("New Service created: $service")
        carRepository.addCarService(service)
    }

    fun onServiceUpdate(service: Service) {
        Timber.d("Service being updated: $service")
        carRepository.updateCarService(service)
    }

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        carRepository.removeCarService(service)
    }

    fun schedulePastDueAlarm() {
        alarms.schedulePastDueAlarm()
    }

    fun onNotificationPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) { removeNotificationPermissionMessage() }
    }

    //////////////////////// MOVE THIS INTO A MessagesRepository ////////////////////////////
    fun addNotificationPermissionMessage() {
        Timber.v("Adding 'Missing Alarms Permissions' message to the queue")
        addMessage(Message.MISSING_PERMISSION_NOTIFICATION)
        setState(state, savedState, STATE) {
            copy(hasPromptedForPermissionNotification = true)
        }
    }

    private fun removeNotificationPermissionMessage() {
        Timber.v("Removing 'Missing Notifications Permission' message from queue")
        removeMessage(Message.MISSING_PERMISSION_NOTIFICATION)
    }

    fun addAlarmPermissionMessage() {
        Timber.v("Adding 'Missing Alarms Permission' message to the queue")
        addMessage(Message.MISSING_PERMISSION_ALARM)
        setState(state, savedState, STATE) {
            copy(hasPromptedForPermissionAlarm = true)
        }
    }

    fun removeAlarmPermissionMessage() {
        Timber.v("Removing 'Missing Alarms Permission' message from queue")
        removeMessage(Message.MISSING_PERMISSION_ALARM)
    }

    fun addTestMessage() = addMessage(Message.TEST)

    fun onMessageDismissed() {
        queueState.value?.let {
            updateQueueState(it.messageQueue.apply { pop() })
        }
    }

    private fun addMessage(message: Message) {
        queueState.value?.let {
            if (it.messageQueue.contains(message.data.id)) {
                return // skip dupes
            }
            Timber.d("Added message with id=${message.data.id} from queue")
            updateQueueState(it.messageQueue.apply { add(message.data) })
        }
    }

    private fun removeMessage(message: Message) {
        with(message.data) {
            queueState.value?.let {
                if (it.messageQueue.contains(id)) {
                    Timber.d("Removed message with id=${message.data.id} from queue")
                    updateQueueState(it.messageQueue.apply { remove(id) })
                }
            }
        }
    }
    //^^^^^^^^^^^^^^^^^^^^^^ MOVE THIS INTO A MessagesRepository ^^^^^^^^^^^^^^^^^^^^^^^^^//

    //vvvvvvvvvvvvvvv MOVE THIS INTO Extension Functions or Helper class vvvvvvvvvvvvvvvvv//
    fun onSortingByType(type: SortingCallback.SortingType) {
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
    //^^^^^^^^^^^^^^ MOVE THIS INTO Extension Functions or Helper class ^^^^^^^^^^^^^^^^//

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    private fun updateQueueState(queue: UniqueMessageQueue) {
        setState(queueState, savedState, QUEUE_STATE) { copy(messageQueue = queue)}
    }

    fun isNight(): Boolean = appSettingsRepository.fetchNightThemeSetting() ?: false

    fun setupEasterEggForTesting() {
        state.value?.car?.let {
            carRepository.saveCarData(it.copy(
                services = serviceList
            ))
        }
    }

    companion object {
        private const val STATE: String = "OverviewViewModel.STATE"
        private const val QUEUE_STATE: String = "OverviewViewModel.QUEUE_STATE"
    }
}