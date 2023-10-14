package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ivan Garza Bermea.
 */
interface CarRepository {
    fun observeCarData(): Flow<Car?>
    fun fetchCarData(): Car?
    fun saveCarData(car: Car)
    fun deleteCarData()
    fun addCarService(service: Service)
    fun removeCarService(service: Service)
    fun updateCarService(service: Service)
}