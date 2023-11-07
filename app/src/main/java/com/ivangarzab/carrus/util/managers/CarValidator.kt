package com.ivangarzab.carrus.util.managers

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivangarzab.carrus.data.models.Car

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
            jsonObject.get("version").let { version ->
                when (version.asInt) {
                    Car.VERSION_CAR -> gson.fromJson(jsonObject, Car::class.java)
                    else -> upgradeCarJsonFromVersion0ToVersion1(jsonObject)
                }
                /*if (version == null) {

                } else {

                }*/
            }
        } catch (e: Exception) {
            //TODO: Log error
            Car.empty
        }
    } ?: Car.empty

    @VisibleForTesting
    fun upgradeCarJsonFromVersion0ToVersion1(jsonObject: JsonObject): Car {
        val milesPerGal = jsonObject.get("milesPerGallon")
        var result = Gson().fromJson(jsonObject, Car::class.java)
        milesPerGal?.let {
            result = result.copy(
                milesPerGalCity = it.asString,
                milesPerGalHighway = it.asString
            )
        }
        return result
    }
}