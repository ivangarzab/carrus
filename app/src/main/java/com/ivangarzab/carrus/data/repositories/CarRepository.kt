package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository @Inject constructor() {

    private val carDataChannel = MutableStateFlow(fetchCarData())

    fun observeCarData(): Flow<Car?> = carDataChannel.asStateFlow()

    fun fetchCarData(): Car? = prefs.defaultCar

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Timber.d("Car data was saved")
        setCarDataChannel(car)
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Timber.d("Car data was removed")
        setCarDataChannel(null)
    }

    private fun setCarDataChannel(car: Car?) = appScope.launch {
        carDataChannel.value = car
    }
}