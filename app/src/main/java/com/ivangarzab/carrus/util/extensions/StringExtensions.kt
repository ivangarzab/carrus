package com.ivangarzab.carrus.util.extensions

import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
fun String.getCalendarFromShortenedDate(): Calendar =
    this.split("/").let {
        Calendar.getInstance().apply {
            timeInMillis = 0L
            if (it.size > 2) {
                set(Calendar.MONTH, it[0].toInt())
                set(Calendar.DAY_OF_MONTH, it[1].toInt())
                set(Calendar.YEAR, it[2].toInt())
            }
        }
    }