package com.ivangarzab.carbud.data.repositories

import android.util.Log
import com.ivangarzab.carbud.appScope
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository {

    private val carDataChannel = MutableStateFlow(fetchCarData())

    fun observeCarData(): Flow<Car?> = carDataChannel.asStateFlow()

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Log.d("IGB", "Car was saved: $car")
        setCarDataChannel(fetchCarData())
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Log.d("IGB", "Car was removed")
        setCarDataChannel(fetchCarData())
    }

    private fun fetchCarData(): Car? = prefs.defaultCar

    private fun setCarDataChannel(car: Car?) {
        Log.d("IGB", "Setting Car data channel: $car")
        appScope.launch {
            carDataChannel.value = car
        }
    }
}