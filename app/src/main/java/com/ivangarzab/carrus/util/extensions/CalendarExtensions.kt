package com.ivangarzab.carrus.util.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by Ivan Garza Bermea.
 */
fun Calendar.getFormattedDate(): String = SimpleDateFormat(
    "MMMM dd, yyyy",
    Locale.US
).let { format ->
    format.format(this.time)
}

fun Calendar.getShortenedDate(): String = SimpleDateFormat(
    "MM/dd/yy",
    Locale.US
).let { format ->
    format.format(this.time)
}

fun Calendar.empty(): Calendar = this.apply { timeInMillis = 0L }