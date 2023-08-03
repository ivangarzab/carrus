package com.ivangarzab.carrus.ui.settings.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.TimeFormat
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
    val clockTimeFormat: TimeFormat = TimeFormat.HR24,
    val timeFormatOptions: List<String> = pickerOptionsTimeFormat,
    val alarmsOn: Boolean = false,
    val alarmTime: String? = null,
    val alarmTimeOptions: List<String> = clockTimeFormat.range.map { it.toString() },
    val alarmFrequency: AlarmFrequency = AlarmFrequency.DAILY,
    val alarmFrequencyOptions: List<String> = pickerOptionsAlarmFrequency,
) : Parcelable

private val pickerOptionsTimeFormat = listOf(
    TimeFormat.HR12.value,
    TimeFormat.HR24.value,
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
