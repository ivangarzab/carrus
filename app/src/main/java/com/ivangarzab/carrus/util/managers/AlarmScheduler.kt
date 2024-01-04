package com.ivangarzab.carrus.util.managers

import com.ivangarzab.carrus.data.alarm.AlarmSchedulingData

interface AlarmScheduler {
    fun scheduleAlarm(alarmData: AlarmSchedulingData, onDone: (Boolean) -> Unit)
    fun cancelAlarm(alarmData: AlarmSchedulingData, onDone: (Boolean) -> Unit)
}