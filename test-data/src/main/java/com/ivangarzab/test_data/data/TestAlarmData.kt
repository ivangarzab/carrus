package com.ivangarzab.test_data.data

import com.ivangarzab.carrus.data.alarm.Alarm
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmSchedulingData
import com.ivangarzab.test_data.STRING_TEST

val ALARM_DATA_TEST: AlarmSchedulingData = AlarmSchedulingData(
    type = Alarm.TEST,
    frequency = AlarmFrequency.DAILY,
    triggerTime = 0L,
    intentRequestCode = 0,
    intentAction = STRING_TEST
)