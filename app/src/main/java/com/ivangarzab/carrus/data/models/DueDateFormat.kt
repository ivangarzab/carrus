package com.ivangarzab.carrus.data.models

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
            WEEKS.value -> WEEKS
            MONTHS.value -> MONTHS
            DATE.value -> DATE
            else -> DAYS
        }
    }
}