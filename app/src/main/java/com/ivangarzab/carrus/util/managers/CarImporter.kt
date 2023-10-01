package com.ivangarzab.carrus.util.managers

import com.google.gson.Gson
import com.ivangarzab.carrus.data.Car
import timber.log.Timber
import java.util.UUID

/**
 * Created by Ivan Garza Bermea.
 */
object CarImporter {

    fun importFromJson(json: String): Car? {
        return try {
            Gson().fromJson(json, Car::class.java).let { car ->
                Timber.d("Got car data to import: $car")
                cleanImportedData(car)
            }
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            null
        }
    }

    /**
     * TODO: There's got to be a better way of doing this!
     *  Or at least arrive at at state where we don't need this anymore --
     *  Should we start versioning our Car data too?
     *
     * TODO: Consider bringing in Moshi for this and use instead of Gson
     */
    private fun cleanImportedData(data: Car): Car = Car(
        uid = UUID.randomUUID().toString(),
        nickname = data.nickname ?: "",
        make = data.make ?: "",
        model = data.model ?: "",
        year = data.year ?: "",
        licenseNo = data.licenseNo ?: "",
        vinNo = data.vinNo ?: "",
        tirePressure = data.tirePressure ?: "",
        totalMiles = data.totalMiles ?: "",
        milesPerGallon = data.milesPerGallon ?: "",
        services = data.services,
        imageUri = null
    )
}