package com.ivangarzab.carbud.ui.overview

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carbud.carRepository
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.util.extensions.setState
import com.ivangarzab.carbud.prefs
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    @Parcelize
    data class OverviewState(
        val car: Car? = null
    ) : Parcelable

    val state: LiveData<OverviewState> = savedState.getLiveData(
        STATE,
        OverviewState()
    )

    var datesInMillis: Pair<Long, Long> = Pair(0, 0)

    init {
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
    }

    fun deleteCarData() = carRepository.deleteCarData().also {
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