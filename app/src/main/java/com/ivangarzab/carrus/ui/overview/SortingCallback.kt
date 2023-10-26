package com.ivangarzab.carrus.ui.overview

/**
 * Created by Ivan Garza Bermea.
 */
interface SortingCallback {
    enum class SortingType {
        NONE, NAME, DATE
    } //TODO: Have this on it's own file
    fun onSort(type: SortingType)
}