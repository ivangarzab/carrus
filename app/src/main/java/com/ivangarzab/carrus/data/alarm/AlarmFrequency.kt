package com.ivangarzab.carrus.data.alarm

import android.app.AlarmManager

/**
 * Created by Ivan Garza Bermea.
 */
enum class AlarmFrequency(
    val value: String,
    val interval: Long
) {
    DAILY("daily", AlarmManager.INTERVAL_DAY),
    OTHER_DAY("every other day", INTERVAL_EVERY_OTHER_DAY),
    WEEKLY("weekly", INTERVAL_WEEKLY),
    /*MONDAYS("weekly on Mondays", INTERVAL_WEEKLY),
    FRIDAYS("weekly on Fridays", INTERVAL_WEEKLY),
    SUNDAY("weekly on Sundays", INTERVAL_WEEKLY),*/
    ;

    companion object {
        fun get(value: String): AlarmFrequency = when (value) {
            OTHER_DAY.value -> OTHER_DAY
            WEEKLY.value -> WEEKLY
/*            MONDAYS.value -> MONDAYS
            FRIDAYS.value -> FRIDAYS
            SUNDAY.value -> SUNDAY*/
            else -> DAILY
        }

        fun asStringList(): List<String> = listOf(
            DAILY.value,
            OTHER_DAY.value,
            WEEKLY.value
        )
    }
}

private const val INTERVAL_EVERY_OTHER_DAY: Long = AlarmManager.INTERVAL_DAY * 2
private const val INTERVAL_WEEKLY: Long = AlarmManager.INTERVAL_DAY * 7