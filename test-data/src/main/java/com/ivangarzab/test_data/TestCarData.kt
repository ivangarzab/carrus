package com.ivangarzab.test_data

import com.ivangarzab.carrus.data.models.Car

/**
 * Created by Ivan Garza Bermea.
 */

val EMPTY_CAR = Car(
    uid = "0",
    nickname = "",
    make = "",
    model = "",
    year = "",
    licenseState = "",
    licenseNo = "",
    vinNo = "",
    tirePressure = "",
    totalMiles = "",
    milesPerGalCity = "",
    milesPerGalHighway = "",
    services = emptyList()
)

val TEST_CAR = Car(
    uid = "1",
    nickname = "testy",
    make = "Android",
    model = "",
    year = "2023",
    licenseState = "Texas",
    licenseNo = "2637642",
    vinNo = "4Y1SL65848Z411439",
    tirePressure = "35",
    totalMiles = "99,999",
    milesPerGalCity = "26",
    milesPerGalHighway = "31",
    services = emptyList()
)