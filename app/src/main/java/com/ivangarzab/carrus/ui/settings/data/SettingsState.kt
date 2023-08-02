package com.ivangarzab.carrus.ui.settings.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class SettingsState(
    val car: Car? = null,
    val dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    val dateFormatOptions: List<String> = pickerOptionsDueDateFormat,
    val alarmsOn: Boolean = false,
    val alarmTime: String? = null,
    val alarmTimeOptions: List<String> = pickerOptionsAlarmTime,
    val alarmFrequency: AlarmFrequency = AlarmFrequency.DAILY,
    val alarmFrequencyOptions: List<String> = pickerOptionsAlarmFrequency,
) : Parcelable

private val pickerOptionsAlarmTime = listOf(
    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
)
private val pickerOptionsDueDateFormat = listOf(
    DueDateFormat.DAYS.value,
    DueDateFormat.WEEKS.value,
    DueDateFormat.MONTHS.value,
    DueDateFormat.DATE.value
)

private val pickerOptionsAlarmFrequency = listOf(
    AlarmFrequency.DAILY.value,
    AlarmFrequency.OTHER_DAY.value,
    AlarmFrequency.MONDAYS.value,
    AlarmFrequency.FRIDAYS.value,
    AlarmFrequency.SUNDAY.value
)
