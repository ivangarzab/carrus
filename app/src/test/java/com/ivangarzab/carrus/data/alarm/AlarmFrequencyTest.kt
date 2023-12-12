package com.ivangarzab.carrus.data.alarm

import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmFrequencyTest {

    @Test
    fun `test get`() {
        assertEquals(AlarmFrequency.DAILY, AlarmFrequency.get("daily"))
        assertEquals(AlarmFrequency.OTHER_DAY, AlarmFrequency.get("every other day"))
        assertEquals(AlarmFrequency.WEEKLY, AlarmFrequency.get("weekly"))
        assertEquals(AlarmFrequency.DAILY, AlarmFrequency.get("invalid")) // Test default case
    }

    @Test
    fun `test asStringList`() {
        val frequencyList = AlarmFrequency.asStringList()
        assertEquals(3, frequencyList.size)
        assertEquals("daily", frequencyList[0])
        assertEquals("every other day", frequencyList[1])
        assertEquals("weekly", frequencyList[2])
    }
}
