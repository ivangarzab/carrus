package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime

/**
 * Created by Ivan Garza Bermea.
 */
data class AlarmSettingsState(
    val isAlarmPermissionGranted: Boolean = false,
    val isAlarmFeatureEnabled: Boolean = false,
    val alarmTime: AlarmTime = AlarmTime.default,
    val frequency: AlarmFrequency = AlarmFrequency.DAILY
)
