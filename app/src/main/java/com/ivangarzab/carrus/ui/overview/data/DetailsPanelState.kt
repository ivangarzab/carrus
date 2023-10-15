package com.ivangarzab.carrus.ui.overview.data

import com.ivangarzab.carrus.data.models.Car

/**
 * Created by Ivan Garza Bermea.
 */
data class DetailsPanelState(
    val licenseState: String = "",
    val licenseNo: String = "",
    val vinNo: String = "",
    val tirePressure: String = "",
    val totalMiles: String = "",
    val milesPerGalCity: String = "",
    val milesPerGalHighway: String = ""
) {
    fun asList(): List<String> {
        val result: MutableList<String> = mutableListOf()
        if (licenseState.isNotBlank()) result.add(licenseState)
        if (licenseNo.isNotBlank()) result.add(licenseNo)
        if (vinNo.isNotBlank()) result.add(vinNo)
        if (tirePressure.isNotBlank()) result.add(tirePressure)
        if (totalMiles.isNotBlank()) result.add(totalMiles)
        if (milesPerGalCity.isNotBlank()) result.add(milesPerGalCity)
        if (milesPerGalHighway.isNotBlank()) result.add(milesPerGalHighway)
        return result
    }

    companion object {
        fun fromCar(car: Car): DetailsPanelState = DetailsPanelState(
            licenseState = "",
            licenseNo = car.licenseNo,
            vinNo = car.vinNo,
            tirePressure = car.tirePressure,
            totalMiles = car.totalMiles,
            milesPerGalCity = car.milesPerGallon,
            milesPerGalHighway = car.milesPerGallon
        )
    }
}
