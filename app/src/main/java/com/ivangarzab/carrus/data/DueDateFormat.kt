package com.ivangarzab.carrus.data

/**
 * Created by Ivan Garza Bermea.
 */
enum class DueDateFormat(
    val value: String
) {
    DAYS("days"),
    WEEKS("weeks"),
    MONTHS("months"),
    DATE("due date")
    ;

    companion object {
        fun get(value: String): DueDateFormat = when (value) {
            "weeks" -> WEEKS
            "months" -> MONTHS
            "due date" -> DATE
            else -> DAYS
        }
    }
}