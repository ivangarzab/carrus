package com.ivangarzab.carrus.ui.overview.data

import com.ivangarzab.carrus.data.models.Car

/**
 * Created by Ivan Garza Bermea.
 */
data class DetailsPanelState(
    val year: String = "",
    val licenseState: String = "",
    val licenseNo: String = "",
    val vinNo: String = "",
    val tirePressure: String = "",
    val totalMiles: String = "",
    val milesPerGalCity: String = "",
    val milesPerGalHighway: String = ""
) {
    fun getTotalValidFields(): Int {
        var result: Int = 0
        if (year.isNotEmpty()) result+=1
        if (licenseState.isNotEmpty()) result+=1
        if (licenseNo.isNotEmpty()) result+=1
        if (vinNo.isNotEmpty()) result+=1
        if (tirePressure.isNotEmpty()) result+=1
        if (totalMiles.isNotEmpty()) result+=1
        if (milesPerGalCity.isNotEmpty()) result+=1
        if (milesPerGalHighway.isNotEmpty()) result+=1
        return result
    }

    companion object {
        fun fromCar(car: Car): DetailsPanelState = DetailsPanelState(
            year = car.year,
            licenseState = car.licenseState,
            licenseNo = car.licenseNo,
            vinNo = car.vinNo,
            tirePressure = car.tirePressure,
            totalMiles = car.totalMiles,
            milesPerGalCity = car.milesPerGalCity,
            milesPerGalHighway = car.milesPerGalCity
        )
    }
}
