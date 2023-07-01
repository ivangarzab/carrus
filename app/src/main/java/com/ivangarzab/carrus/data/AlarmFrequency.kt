package com.ivangarzab.carrus.data

/**
 * Created by Ivan Garza Bermea.
 */
enum class AlarmFrequency(
    val value: String
) {
    DAILY("daily"),
    OTHER_DAY("every other day"),
    MONDAYS("weekly on Mondays"),
    FRIDAYS("weekly on Fridays"),
    SUNDAY("weekly on Sundays"),
    ;

    companion object {
        fun get(value: String): AlarmFrequency = when (value) {
            OTHER_DAY.value -> OTHER_DAY
            MONDAYS.value -> MONDAYS
            FRIDAYS.value -> FRIDAYS
            SUNDAY.value -> SUNDAY
            else -> DAILY
        }
    }
}