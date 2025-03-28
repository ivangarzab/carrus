package com.ivangarzab.carrus.data.models

/**
 * Created by Ivan Garza Bermea.
 */
enum class TimeFormat(
    val value: String,
    val range: IntRange
) {
    HR12("12-hour", 1..12),
    HR24("24-hour", 0..23)
    ;

    companion object {
        fun get(value: String): TimeFormat = when (value) {
            HR24.value -> HR24
            else -> HR12
        }
    }
}