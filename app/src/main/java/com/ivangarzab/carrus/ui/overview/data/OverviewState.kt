package com.ivangarzab.carrus.ui.overview.data

import com.ivangarzab.carrus.data.models.Service

/**
 * Created by Ivan Garza Bermea.
 */
data class OverviewStaticState(
    val isDataEmpty: Boolean = true,
    val carName: String = "",
    val imageUri: String? = null
)

data class OverviewServicesState(
    val serviceItemList: List<ServiceItemState> = emptyList(),
    val showSortingOptions: Boolean = false,
    val selectedSortingOption: Int = 0
)

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
