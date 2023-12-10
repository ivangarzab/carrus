package com.ivangarzab.carrus.data.alarm

import junit.framework.TestCase.assertEquals
import org.junit.Test

class AlarmSchedulingDataTest {

    @Test
    fun `test alarm scheduling data`() {
        val alarmType = Alarm.TEST
        val frequency = AlarmFrequency.DAILY
        val triggerTime = System.currentTimeMillis() // Sample trigger time
        val intentRequestCode = 123
        val intentAction = "com.example.ACTION"

        val alarmSchedulingData = AlarmSchedulingData(alarmType, frequency, triggerTime, intentRequestCode, intentAction)

        assertEquals(alarmType, alarmSchedulingData.type)
        assertEquals(frequency, alarmSchedulingData.frequency)
        assertEquals(triggerTime, alarmSchedulingData.triggerTime)
        assertEquals(intentRequestCode, alarmSchedulingData.intentRequestCode)
        assertEquals(intentAction, alarmSchedulingData.intentAction)
    }
}