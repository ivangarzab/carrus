package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ivan Garza Bermea.
 */
interface CarRepository {

    val carDataFlow: StateFlow<Car?>

    fun observeCarData(): Flow<Car?>
    fun fetchCarData(): Car?
    fun saveCarData(car: Car)
    fun deleteCarData()
    fun addCarService(service: Service)
    fun removeCarService(service: Service)
    fun updateCarService(service: Service)
}