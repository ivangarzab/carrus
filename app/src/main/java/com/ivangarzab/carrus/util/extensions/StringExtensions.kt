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

/**
 * As copied from: https://stackoverflow.com/questions/75112197/method-isdigitsonly-in-android-text-textutils-not-mocked-error
 */
fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()

/**
 * Takes a [String] and attempts to convert into a [Float] value representing money
 * with the schema of "N.MO", or return [EMPTY_MONEY_VALUE] if the input is invalid.
 */
fun String.parseIntoMoney(): Float {
    if (this.isBlank()) return EMPTY_MONEY_VALUE
    val result: Float = when (this.isDigitsOnly()) {
        true -> this.toFloat()
        false -> {
            this.split(".").let { s ->
                s.forEach {
                    if (it.isDigitsOnly().not()) return EMPTY_MONEY_VALUE
                }
            }
            this.toFloat()
        }
    }
    return result
}
const val EMPTY_MONEY_VALUE = 0.00f