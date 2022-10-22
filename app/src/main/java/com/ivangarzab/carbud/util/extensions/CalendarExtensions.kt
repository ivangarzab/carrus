package com.ivangarzab.carbud.util.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
fun Calendar.getFormattedDate(): String = SimpleDateFormat(
    getPreferredDateFormat(),
    Locale.US
).let { format ->
    format.format(this.time)
}

fun Calendar.getPreferredDateFormat(): String = "MMMM dd, yyyy"