package com.ivangarzab.carrus.data.repositories

import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.EMPTY_CAR
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by Ivan Garza Bermea.
 */
class TestCarRepository : CarRepository {

    @VisibleForTesting
    var carData: Car? = EMPTY_CAR

    override fun observeCarData(): Flow<Car?> {
        return MutableStateFlow(carData)
    }

    override fun fetchCarData(): Car? {
        return carData
    }

    override fun saveCarData(car: Car) {
        carData = car
    }

    override fun deleteCarData() {
        carData = null
    }

    override fun addCarService(service: Service) {
        carData?.let {
            saveCarData(it.apply {
                services = services.toMutableList().apply { add(service) }
            })
        }
    }

    override fun removeCarService(service: Service) {
        carData?.let {
            saveCarData(it.apply {
                services = services.toMutableList().apply { remove(service) }
            })
        }
    }

    override fun updateCarService(service: Service) {
        carData?.let {car ->
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