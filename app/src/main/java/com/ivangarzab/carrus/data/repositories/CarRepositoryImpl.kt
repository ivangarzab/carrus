package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class CarRepositoryImpl @Inject constructor() : CarRepository {

    private val _carDataChannel: MutableStateFlow<Car?> = MutableStateFlow(null)
    override val carDataChannel: StateFlow<Car?>
        get() = _carDataChannel

    init {
        updateCarDataChannel(fetchCarData())
    }

    private fun updateCarDataChannel(car: Car?) = appScope.launch {
        _carDataChannel.value = car
    }

    override fun observeCarData(): Flow<Car?> {
        return carDataChannel
    }

    override fun fetchCarData(): Car? = prefs.defaultCar

    override fun saveCarData(car: Car) {
        prefs.defaultCar = car
        Timber.d("Car data was saved")
        updateCarDataChannel(car)
    }

    override fun deleteCarData() {
        prefs.defaultCar = null
        Timber.d("Car data was removed")
        updateCarDataChannel(null)
    }

    override fun addCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Adding car service: $service")
        saveCarData(it.apply {
            services = services.toMutableList().apply { add(service) }
        })
    } ?: Timber.w("There's no car data to update")

    override fun removeCarService(service: Service) = fetchCarData()?.let {
        Timber.d("Removing car service: $service")
        saveCarData(it.apply {
            services = services.toMutableList().apply { remove(service) }
        })
    } ?: Timber.w("There's no car data to update")

    override fun updateCarService(service: Service) = fetchCarData()?.let { car ->
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