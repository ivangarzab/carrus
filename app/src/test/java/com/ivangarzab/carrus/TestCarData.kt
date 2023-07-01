package com.ivangarzab.carrus

import com.ivangarzab.carrus.data.Car

/**
 * Created by Ivan Garza Bermea.
 */

val EMPTY_CAR = Car(
    uid = "0",
    nickname = "",
    make = "",
    model = "",
    year = "",
    licenseNo = "",
    vinNo = "",
    tirePressure = "",
    totalMiles = "",
    milesPerGallon = "",
    services = emptyList()
)

val TEST_CAR = Car(
    uid = "1",
    nickname = "testy",
    make = "Android",
    model = "",
    year = "2023",
    licenseNo = "2637642",
    vinNo = "4Y1SL65848Z411439",
    tirePressure = "35",
    totalMiles = "99,999",
    milesPerGallon = "26",
    services = emptyList()
)