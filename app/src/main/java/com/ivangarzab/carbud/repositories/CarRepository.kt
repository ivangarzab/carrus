package com.ivangarzab.carbud.repositories

import android.util.Log
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.prefs

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepository {

    fun getDefaultCar(): Car? = prefs.defaultCar

    fun saveCar(car: Car) {
        prefs.defaultCar = car
        Log.d("IGB", "Default car was saved: $car")
    }

    fun deleteDefaultCar() {
        prefs.defaultCar = null
        Log.d("IGB", "Default car was removed")
    }
}