package com.ivangarzab.carrus.data.repositories

import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

/**
 * Created by Ivan Garza Bermea.
 */
class TestCarRepository : CarRepository {

    @VisibleForTesting
    val _carDataChannel: MutableStateFlow<Car?> = MutableStateFlow(null)
    override val carDataChannel: StateFlow<Car?>
        get() = _carDataChannel

    private fun updateCarDataChannel(car: Car?) = runBlocking {
        _carDataChannel.value = car
    }

    override fun observeCarData(): Flow<Car?> {
        return carDataChannel
    }

    override fun fetchCarData(): Car? {
        return carDataChannel.value
    }

    override fun saveCarData(car: Car) {
        updateCarDataChannel(car)
    }

    override fun deleteCarData() {
        updateCarDataChannel(null)
    }

    override fun addCarService(service: Service) {
        carDataChannel.value?.let {
            saveCarData(it.apply {
                services = services.toMutableList().apply { add(service) }
            })
        }
    }

    override fun removeCarService(service: Service) {
        carDataChannel.value?.let {
            saveCarData(it.apply {
                services = services.toMutableList().apply { remove(service) }
            })
        }
    }

    override fun updateCarService(service: Service) {
        carDataChannel.value?.let { car ->
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
        }
    }
}