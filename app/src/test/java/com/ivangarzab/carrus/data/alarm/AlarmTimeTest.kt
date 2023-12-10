package com.ivangarzab.carrus.data.alarm

import com.ivangarzab.carrus.data.models.TimeFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmTimeTest {

    @Test
    fun `test getTime while isPM`() {
        val alarmTime = AlarmTime(raw24HourValue = 13) // Sample raw value

        assertEquals(13, alarmTime.getTime(TimeFormat.HR24))
        assertEquals(1, alarmTime.getTime(TimeFormat.HR12))
    }

    @Test
    fun `test getTime without isPM`() {
        val alarmTime = AlarmTime(raw24HourValue = 7) // Sample raw value

        assertEquals(7, alarmTime.getTime(TimeFormat.HR24))
        assertEquals(1, alarmTime.getTime(TimeFormat.HR12))
    }

    @Test
    fun `test getTimeAsString`() {
        val alarmTime = AlarmTime(raw24HourValue = 8) // Sample raw value

        assertEquals("8:00", alarmTime.getTimeAsString(TimeFormat.HR24))
        assertEquals("8 AM", alarmTime.getTimeAsString(TimeFormat.HR12))
    }

    @Test
    fun `test equals`() {
        val alarmTime1 = AlarmTime(raw24HourValue = 10)
        val alarmTime2 = AlarmTime(raw24HourValue = 10)
        val alarmTime3 = AlarmTime(raw24HourValue = 12)

        assertEquals(true, alarmTime1.equals(alarmTime2))
        assertEquals(false, alarmTime1.equals(alarmTime3))
    }

    @Test
    fun `test toString`() {
        val alarmTime = AlarmTime(raw24HourValue = 9) // Sample raw value

        assertEquals("9:00", alarmTime.toString())
    }

    @Test
    fun `test companion object default`() {
        val defaultAlarmTime = AlarmTime.default

        assertEquals(7, defaultAlarmTime.raw24HourValue)
    }

    // You can add more test cases here as needed
}
