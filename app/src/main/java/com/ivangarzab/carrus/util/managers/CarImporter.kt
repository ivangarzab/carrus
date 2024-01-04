package com.ivangarzab.carrus.util.managers

import com.google.gson.Gson
import com.ivangarzab.carrus.data.models.Car
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class CarImporter @Inject constructor() {

    /**
     * Using the JSON received as a parameter, utilize [Gson] in order to import the data.
     * This function may fail for a few different reasons, and it may produce a end-result which
     * contains null values for fields that are not nullable.
     *
     * @param json the JSON string to use for importation
     *
     * TODO: Consider bringing in Moshi for this and use instead of Gson
     *      *  Should we start versioning our Car data too?
     */
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
     * Clean up the freshly imported [Car] data as to make sure that there are no null values
     * for fields that are not expecting null values.  Additionally, we're nullifying the
     * 'imageUri' field as to avoid any trouble with the storage rules.
     */
    @Suppress("USELESS_ELVIS")
    private fun cleanImportedData(data: Car): Car = Car(
        uid = UUID.randomUUID().toString(),
        nickname = data.nickname ?: "",
        make = data.make ?: "",
        model = data.model ?: "",
        year = data.year ?: "",
        licenseState = data.licenseState ?: "",
        licenseNo = data.licenseNo ?: "",
        vinNo = data.vinNo ?: "",
        tirePressure = data.tirePressure ?: "",
        totalMiles = data.totalMiles ?: "",
        milesPerGalCity = data.milesPerGalCity ?: "",
        milesPerGalHighway = data.milesPerGalHighway ?: "",
        services = data.services,
        imageUri = null
    )
}