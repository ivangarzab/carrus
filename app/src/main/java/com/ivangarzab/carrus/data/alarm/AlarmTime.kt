package com.ivangarzab.carrus.data.alarm

import android.os.Parcelable
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class AlarmTime(
    /** range between 0 - 23 */
    private val raw24HourValue: Int = 7
) : Parcelable {

    val isPM: Boolean = raw24HourValue > 12

    /**
     * Return the current alarm time in a 24-hour clock format.
     */
    fun getTime(timeFormat: TimeFormat): Int = when (timeFormat) {
        TimeFormat.HR24 -> raw24HourValue
        TimeFormat.HR12 -> when (isPM) {
            true -> raw24HourValue + 12
            false -> raw24HourValue
        }
    }

    /**
     * Return the current alarm time in a 24-hour clock format, as a formatted String.
     */
    fun getTimeAsString(timeFormat: TimeFormat): String = when (timeFormat) {
        TimeFormat.HR24 -> "${raw24HourValue}:00"
        TimeFormat.HR12 -> when (isPM) {
            true -> "${raw24HourValue - 12} PM"
            false -> "$raw24HourValue AM"
        }
    }

    /**
     * Compare this [AlarmTime] with another instance of the same class.
     *
     * @return true if both objects contain the same value; false otherwise
     */
    fun equals(other: AlarmTime): Boolean = this.raw24HourValue == other.raw24HourValue

    override fun toString(): String {
        return "$raw24HourValue:00"
    }

    companion object {
        val default = AlarmTime(raw24HourValue = DEFAULT_ALARM_TIME)
    }
}
