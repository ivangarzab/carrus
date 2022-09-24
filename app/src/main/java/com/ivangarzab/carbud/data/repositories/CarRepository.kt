package com.ivangarzab.carbud.data.repositories

import android.util.Log
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository {

    private val carDataChannel = MutableStateFlow(fetchCarData())

    fun observeCarData(): StateFlow<Car?> = carDataChannel.asStateFlow()

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Log.d("IGB", "Default car was saved: $car")
        setCarData(fetchCarData())
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Log.d("IGB", "Default car was removed")
        setCarData(fetchCarData())
    }

    private fun fetchCarData(): Car? = prefs.defaultCar

    private fun setCarData(car: Car?) {
        carDataChannel.value = car
    }
}