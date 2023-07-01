package com.ivangarzab.carrus.data

/**
 * Created by Ivan Garza Bermea.
 */
enum class TimeFormat(
    val value: String
) {
    HR12("12 hours"),
    HR24("24 hours")
    ;

    companion object {
        fun get(value: String): TimeFormat = when (value) {
            "24 hours" -> HR24
            else -> HR12
        }
    }
}