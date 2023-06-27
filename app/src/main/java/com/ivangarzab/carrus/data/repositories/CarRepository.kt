package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository {

    private val carDataChannel = MutableStateFlow(fetchCarData())

    private fun updateCarDataChannel(car: Car?) = appScope.launch {
        carDataChannel.value = car
    }

    fun observeCarData(): Flow<Car?> = carDataChannel.asStateFlow()

    fun fetchCarData(): Car? = prefs.defaultCar

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Timber.d("Car data was saved")
        updateCarDataChannel(car)
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Timber.d("Car data was removed")
        updateCarDataChannel(null)
    }

    fun addCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Adding car service: $service")
        saveCarData(it.apply {
            services = services.toMutableList().apply { add(service) }
        })
    } ?: Timber.w("There's no car data to update")

    fun removeCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Removing car service: $service")
        saveCarData(it.apply {
            services = services.toMutableList().apply { remove(service) }
        })
    } ?: Timber.w("There's no car data to update")

    fun updateCarService(service: Service) = fetchCarData()?.let { car ->
        Timber.d("Removing car service ${service.name} to: $service")
        saveCarData(
            car.copy(
                services = car.services.map {
                    when (it.id == service.id) {
                        true -> service
                        false -> it
                    }
                }
            )
        )
    } ?: Timber.w("There's no car data to update")
}