package com.ivangarzab.carbud.overview

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Part
import com.ivangarzab.carbud.extensions.setState
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

    fun fetchDefaultCar() = carRepository.getDefaultCar()?.let {
        updateCarState(it)
    } ?: updateCarState(Car.empty)

    fun onNewPartCreated(part: Part) {
        Log.d("IGB", "Got a new part: $part")
        state.value?.car?.let {
            updateCarState(it.apply {
                parts = parts.toMutableList().apply { add(part) }
            })
        }
    }

    private fun updateCarState(car: Car) =
        setState(state, savedState, STATE) { copy(car = car) }

    companion object {
        private const val STATE: String = "OverviewViewModel.STATE"
    }
}