package com.ivangarzab.carrus.util.extensions

import com.ivangarzab.carrus.data.Service
import java.util.*

/**
 * Boolean return indicating whether a [Service] is past due.
 */
fun Service.isPastDue(): Boolean = this.dueDate.timeInMillis < Calendar.getInstance().timeInMillis

/**
 * Returns the [Service.brand] & [Service.type] as a single string, while available.
 */
fun Service.getDetails(): String {
    val b = brand ?: ""
    val t = type ?: ""
    return when {
        b.isNotEmpty() && t.isNotEmpty() -> "$b - $t"
        b.isNotEmpty() -> b
        t.isNotEmpty() -> t
        else -> "---"
    }
}