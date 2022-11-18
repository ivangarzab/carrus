package com.ivangarzab.carbud.ui.overview

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carbud.*
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.data.serviceList
import com.ivangarzab.carbud.util.extensions.setState
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
        val serviceSortingType: SortingCallback.SortingType = SortingCallback.SortingType.NONE,
        val notificationPermissionState: Boolean = false
    ) : Parcelable

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
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

    fun toggleNotificationPermissionState(granted: Boolean) = setState(state, savedState, STATE) {
        copy(notificationPermissionState = granted)
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
    }
}