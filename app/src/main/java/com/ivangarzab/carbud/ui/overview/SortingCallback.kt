package com.ivangarzab.carbud.ui.overview

/**
 * Created by Ivan Garza Bermea.
 */
interface SortingCallback {
    enum class SortingType {
        NONE, NAME, DATE
    }
    fun onSort(type: SortingType)
}