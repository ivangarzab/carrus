package com.ivangarzab.carrus.util.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by Ivan Garza Bermea.
 */

fun String.getCalendarFromShortenedDate(): Calendar = this.takeIf {
    it.isNotBlank()
}?.let {
    SimpleDateFormat("MM/dd/yy", Locale.US).let { format ->
        Calendar.getInstance().apply {
            time = format.parse(this@getCalendarFromShortenedDate) as Date
        }
    }
} ?: Calendar.getInstance().empty()

fun String.parseIntoMoney(): Float = this.takeIf { it.isNotBlank() }?.toFloat() ?: 0.00f