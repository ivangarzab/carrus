package com.ivangarzab.carrus.util.extensions

import timber.log.Timber
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

/**
 * Parses a [String] into a number with commas format of type: 100,000.
 * TODO: Make tests!
 * More examples include: 1,000; 999,999; 1,000,000; etc.
 */
fun String.parseIntoNumberWithCommas(): String = try {
    String.format("%,d", this.toLong())
} catch (e: NumberFormatException) {
    Timber.w("Caught a NumberFormatException trying to parse $this into a number with commas", e)
    this
}