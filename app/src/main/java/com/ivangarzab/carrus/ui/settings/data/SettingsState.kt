package com.ivangarzab.carrus.ui.settings.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class SettingsState(
    val car: Car? = null,
    val alarmTime: String? = null,
    val dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    val alarmTimeOptions: List<String> = pickerOptionsAlarmTime,
    val dateFormatOptions: List<String> = pickerOptionsDueDateFormat
) : Parcelable

private val pickerOptionsAlarmTime = listOf(
    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
)
private val pickerOptionsDueDateFormat = listOf(
    "days", "weeks", "months", "due date"
)
