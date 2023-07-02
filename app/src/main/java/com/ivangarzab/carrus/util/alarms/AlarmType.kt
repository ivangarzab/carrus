package com.ivangarzab.carrus.util.alarms

import com.ivangarzab.carrus.data.AlarmFrequency

/**
 * Created by Ivan Garza Bermea.
 */
enum class AlarmType {
    TEST,
    PAST_DUE
    ;
    fun getSchedulingData(
        frequency: AlarmFrequency,
        alarmTime: Long
    ): AlarmSchedulingData = when (this) {
        TEST -> AlarmSchedulingData(
            type = this,
            frequency = frequency,
            triggerTime = alarmTime,
            intentRequestCode = 0,
            intentAction = "alarm-test"
        )
        PAST_DUE -> AlarmSchedulingData(
            type = this,
            frequency = frequency,
            triggerTime = alarmTime,
            intentRequestCode = 200,
            intentAction = "alarm-past-due"
        )
    }
}