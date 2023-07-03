package com.ivangarzab.carrus.data

import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */

val SERVICE_EMPTY = Service(
    id = "0",
    name = "",
    repairDate = Calendar.getInstance(),
    dueDate = Calendar.getInstance()
)

val SERVICE_TEST_1 = Service(
    id = "1",
    name = "Service",
    repairDate = Calendar.getInstance().apply {
        timeInMillis = 1687846438000
    },
    dueDate = Calendar.getInstance().apply {
        timeInMillis = 1687846438000
    }
)

val SERVICE_TEST_2 = Service(
    id = "2",
    name = "Servicio",
    repairDate = Calendar.getInstance().apply {
        timeInMillis = 1687846430000
    },
    dueDate = Calendar.getInstance().apply {
        timeInMillis = 1687846430000
    }
)