package com.ivangarzab.carrus.ui.overview

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carrus.*
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.Message
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.serviceList
import com.ivangarzab.carrus.util.extensions.setState
import com.ivangarzab.carrus.util.managers.MessageQueue
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    @Parcelize
    data class OverviewState(
        val car: Car? = null,
        val serviceSortingType: SortingCallback.SortingType = SortingCallback.SortingType.NONE
    ) : Parcelable

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
    )

    @Parcelize
    data class QueueState(
        val messageQueue: MessageQueue = MessageQueue()
    ) : Parcelable

    val queueState: LiveData<QueueState> = savedState.getLiveData(
        QUEUE_STATE,
        QueueState()
    )

    // Pair<repairDate, dueDate>
    var datesInMillis: Pair<Long, Long> = Pair(0, 0)

    init {
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
    }

    fun verifyServiceData(
        name: String
    ): Boolean = name.isNotBlank() && datesInMillis.first != 0L && datesInMillis.second != 0L

    fun onServiceCreated(service: Service) {
        Timber.d("New Service created: $service")
        prefs.apply {
            addService(service)
            defaultCar?.let { carRepository.saveCarData(it) }
        }
    }

    fun onServiceUpdate(service: Service) {
        Timber.d("Service being updated: $service")
        prefs.defaultCar?.let { car ->
            carRepository.saveCarData(
                car.copy(
                    services = car.services.map {
                        when (it.id == service.id) {
                            true -> service
                            false -> it
                        }
                    }
                )
            )
        }
    }

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        prefs.apply {
            deleteService(service)
            defaultCar?.let { carRepository.saveCarData(it) }
        }
    }

    fun schedulePastDueAlarm() {
        alarms.schedulePastDueAlarm()
    }

    fun onPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) {
            queueState.value?.messageQueue?.remove(
                Message.MISSING_PERMISSION_NOTIFICATION.data.id
            )
        } else {

        }
    }

    fun addNotificationPermissionMessage() {
        Timber.v("Adding 'Missing Notification Message' to the queue")
        addMessage(Message.MISSING_PERMISSION_NOTIFICATION)
    }

    fun addTestMessage() = addMessage(Message.TEST)

    private fun addMessage(message: Message) {
        queueState.value?.let {
            if (it.messageQueue.contains(message.data.id)) {
                return // skip dupes
            }
        }
        setState(queueState, savedState, QUEUE_STATE) {
            copy(
                messageQueue = messageQueue.apply {
                    add(message.data)
                }
            )
        }
    }

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

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

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