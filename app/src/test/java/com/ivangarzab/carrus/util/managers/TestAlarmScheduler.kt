package com.ivangarzab.carrus.util.managers

import com.ivangarzab.carrus.data.alarm.AlarmSchedulingData

class TestAlarmScheduler : AlarmScheduler {
    override fun scheduleAlarm(alarmData: AlarmSchedulingData, onDone: (Boolean) -> Unit) {
        //No-op
    }

    override fun cancelAlarm(alarmData: AlarmSchedulingData, onDone: (Boolean) -> Unit) {
        //No-op
    }
}