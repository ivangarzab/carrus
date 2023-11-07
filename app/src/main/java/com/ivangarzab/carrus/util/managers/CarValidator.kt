package com.ivangarzab.carrus.util.managers

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivangarzab.carrus.data.models.Car
import timber.log.Timber

/**
 * The purpose of this class is to upgrade any type of [Car] data into the latest
 * version of the of such type of data, as found in [Car.VERSION_CAR].
 *
 * Created by Ivan Garza Bermea.
 */
object CarValidator {

    fun validateCarData(json: String?): Car = json?.let {
        val gson = Gson()
        try {
            val jsonObject = gson.fromJson(json, JsonObject::class.java)
            if (jsonObject.isEmpty) throw Exception()

            jsonObject.get("version").let { version ->
                when (version == null) {
                    // Covers data prior to versioning
                    true -> upgradeCarJsonFromVersion0ToVersion1(jsonObject)
                    // Version found -- check if we need upgrade
                    false -> when (version.asInt) {
                        Car.VERSION_CAR -> gson.fromJson(jsonObject, Car::class.java)
                        else -> upgradeCarJsonFromVersion0ToVersion1(jsonObject)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.w("Unable to validate car with json: $json")
            Car.empty
        }
    } ?: Car.empty

    @VisibleForTesting
    fun upgradeCarJsonFromVersion0ToVersion1(jsonObject: JsonObject): Car =
        try {
            val milesPerGal = jsonObject.get("milesPerGallon")
            var result = Gson().fromJson(jsonObject, Car::class.java)
            milesPerGal?.let {
                result = result.copy(
                    licenseState = "",
                    milesPerGalCity = it.asString,
                    milesPerGalHighway = it.asString
                )
            }
            result
        } catch (e: Exception) {
            Timber.w("Unable to upgrade json object: $jsonObject")
            Car.empty
        }

}