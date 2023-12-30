package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.test_data.data.ALARM_DATA_TEST
import org.junit.Before
import org.junit.Test

class AlarmSchedulerTest {

    private lateinit var scheduler: AlarmScheduler

    @Before
    fun setup() {
        scheduler = TestAlarmScheduler()
    }

    @Test
    fun test_scheduleAlarm() {
        scheduler.scheduleAlarm(ALARM_DATA_TEST) {
            assertThat(it)
                .isInstanceOf(Boolean::class.java)
        }
    }

    @Test
    fun test_cancelAlarm() {
        scheduler.cancelAlarm(ALARM_DATA_TEST) {
            assertThat(it)
                .isInstanceOf(Boolean::class.java)
        }
    }
}