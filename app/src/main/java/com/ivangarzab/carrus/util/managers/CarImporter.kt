package com.ivangarzab.carrus.util.managers

import com.google.gson.Gson
import com.ivangarzab.carrus.data.Car
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
object CarImporter {

    fun importFromJson(json: String): Car? {
        return try {
            Gson().fromJson(json, Car::class.java).let { car ->
                Timber.d("Got car data to import: $car")
                car
            }
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            null
        }
    }
}