package com.ivangarzab.test_data

import com.ivangarzab.carrus.data.models.Service
import java.util.Calendar

val TEST_SERVICE: Service = Service(
    version = 1,
    id = "1",
    name = "Test",
    repairDate = Calendar.getInstance(),
    dueDate = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 1)
    },
    brand = "testBrand",
    type = "testType",
    cost = 0.0f
)

val TEST_SERVICE_EMPTY: Service = Service(
    version = 1,
    id = "0",
    name = "",
    repairDate = Calendar.getInstance().apply { timeInMillis = 0L },
    dueDate = Calendar.getInstance().apply { timeInMillis = 0L },
    brand = null,
    type = null,
    cost = 0.0f
)