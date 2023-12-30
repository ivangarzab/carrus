package com.ivangarzab.carrus.ui.overview.data

import com.ivangarzab.carrus.data.models.Service

data class ServiceItemState(
    val index: Int,
    val name: String,
    val details: String,
    val price: String,
    val repairDate: String,
    val dueDateFormatted: String,
    val isPastDue: Boolean,
    val data: Service
)
