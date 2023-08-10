package com.ivangarzab.carrus.data

import com.ivangarzab.carrus.data.alarm.AlarmFrequency

/**
 * Created by Ivan Garza Bermea.
 */
data class AlarmSettingsState(
    val isAlarmPermissionGranted: Boolean = false,
    val isAlarmFeatureEnabled: Boolean = false,
    val alarmTime: String = "",
    val frequency: AlarmFrequency = AlarmFrequency.DAILY
)
