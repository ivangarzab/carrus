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
    companion object {
        fun fromCar(car: Car): DetailsPanelState = DetailsPanelState(
            licenseState = "", //TODO: Impl
            licenseNo = car.licenseNo,
            vinNo = car.vinNo,
            tirePressure = car.tirePressure,
            totalMiles = car.totalMiles,
            milesPerGalCity = car.milesPerGallon,
            milesPerGalHighway = car.milesPerGallon
        )
    }
}
