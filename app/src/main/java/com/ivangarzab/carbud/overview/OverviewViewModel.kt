package com.ivangarzab.carbud.overview

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.extensions.setState
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.repositories.CarRepository
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    //TODO: get this thru injection
    private val carRepository = CarRepository()

    @Parcelize
    data class OverviewState(
        val car: Car? = null
    ) : Parcelable

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
    )

    var datesInMillis: Pair<Long, Long> = Pair(0, 0)

    fun fetchDefaultCar() = carRepository.getDefaultCar()?.let {
        updateCarState(it)
    }

    fun deleteCarData() = carRepository.deleteDefaultCar().also {
        updateCarState(null)
    }

    fun verifyServiceData(
        name: String
    ): Boolean = name.isNotBlank() && datesInMillis.first != 0L && datesInMillis.second != 0L

    fun onServiceCreated(service: Service) {
        Log.d("IGB", "New Service created: $service")
        prefs.addService(service)
        state.value?.car?.let {
            updateCarState(it.apply {
                services = services.toMutableList().apply { add(service) }
            })
        }
    }

    fun onServiceDeleted(service: Service) {
        Log.d("IGB", "Service being deleted: $service")
        prefs.deleteService(service)
        state.value?.car?.let {
            updateCarState(it.apply {
                services = services.toMutableList().apply { remove(service) }
            })
        }
    }

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    companion object {
        private const val STATE: String = "OverviewViewModel.STATE"
    }
}