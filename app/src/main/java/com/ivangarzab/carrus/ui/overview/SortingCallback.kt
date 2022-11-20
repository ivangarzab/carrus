package com.ivangarzab.carrus.ui.overview

/**
 * Created by Ivan Garza Bermea.
 */
interface SortingCallback {
    enum class SortingType {
        NONE, NAME, DATE
    }
    fun onSort(type: SortingType)
}