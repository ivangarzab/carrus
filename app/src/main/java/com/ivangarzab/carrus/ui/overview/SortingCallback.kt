package com.ivangarzab.carrus.ui.overview

import com.ivangarzab.carrus.ui.overview.data.SortingType

/**
 * Created by Ivan Garza Bermea.
 */
interface SortingCallback {
    fun onSort(type: SortingType)
}