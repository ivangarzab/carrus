package com.ivangarzab.carrus.ui.overview.data

data class ServicePanelState(
    val serviceItemList: List<ServiceItemState> = emptyList(),
    val showSortingOptions: Boolean = false,
    val selectedSortingOption: Int = 0
)
