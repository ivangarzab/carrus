package com.ivangarzab.carrus.util.managers

import com.google.gson.Gson
import com.ivangarzab.carrus.data.models.Car
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
object CarExporter {

    /**
     * With the [Car] data received as a parameter, use [Gson] to convert the model class into a
     * JSON string that may be used to easily move our data outside of the app.
     */
    fun exportToJson(data: Car): String? {
        return try {
            cleanData(data).let { cleanData ->
                Timber.d("Got car data to export: $cleanData")
                Gson().toJson(cleanData)
            }
        } catch (e: Exception) {
            Timber.w("Unable to export data", e)
            null
        }
    }

    /**
     * Clean up the [Car] data model in order to facilitate export/import functionality.
     */
    private fun cleanData(data: Car): Car = data.copy(imageUri = null)
}