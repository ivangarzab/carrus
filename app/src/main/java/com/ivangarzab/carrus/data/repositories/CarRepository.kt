package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.managers.Analytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class CarRepository @Inject constructor() {

    private val carDataChannel = MutableStateFlow<Car?>(null)

    init {
        updateCarDataChannel(fetchCarData())
    }

    private fun updateCarDataChannel(car: Car?) = appScope.launch {
        carDataChannel.value = car
    }

    fun observeCarData(): Flow<Car?> {
        return carDataChannel.asStateFlow()
    }

    fun fetchCarData(): Car? = prefs.defaultCar

    fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Timber.d("Car data was saved")
        Analytics.logCarCreate(car.uid, car.getCarName())
        updateCarDataChannel(car)
    }

    fun deleteCarData() {
        prefs.defaultCar = null
        Timber.d("Car data was removed")
        fetchCarData()?.let { Analytics.logCarDelete(it.uid, it.getCarName()) }
        updateCarDataChannel(null)
    }

    fun addCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Adding car service: $service")
        Analytics.logServiceCreate(service.id, service.name)
        saveCarData(it.apply {
            services = services.toMutableList().apply { add(service) }
        })
    } ?: Timber.w("There's no car data to update")

    fun removeCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Removing car service: $service")
        Analytics.logServiceDelete(service.id, service.name)
        saveCarData(it.apply {
            services = services.toMutableList().apply { remove(service) }
        })
    } ?: Timber.w("There's no car data to update")

    fun updateCarService(service: Service) = fetchCarData()?.let { car ->
        Timber.d("Updating car service ${service.name} to: \n$service")
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