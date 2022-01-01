package com.ivangarzab.carbud.repositories

import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.prefs

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository {

    fun getDefaultCar(): Car? = prefs.defaultCar

    fun saveCar(car: Car) {
        //TODO
    }

    fun deleteDefaultCar() {
        //TODO
    }

    fun deleteCar(car: Car) {
        //TODO
    }
}