package com.ivangarzab.carbud.data.repositories

import android.util.Log
import com.ivangarzab.carbud.TAG
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

    fun fetchCarData(): Car? = prefs.defaultCar

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Log.d(TAG, "Car data was saved")
        setCarDataChannel(car)
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Log.d(TAG, "Car data was removed")
        setCarDataChannel(null)
    }

    private fun setCarDataChannel(car: Car?) = appScope.launch {
        carDataChannel.value = car
    }
}