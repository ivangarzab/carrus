package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmsRepositoryTest {

    private lateinit var repository: TestAlarmsRepository

    @Before
    fun setup() {
        repository = TestAlarmsRepository()
    }

    @Test
    fun test_isPastDueAlarmActive_base() = with(repository) {
        assertThat(isPastDueAlarmActive())
            .isFalse()
    }

    @Test
    fun test_isPastDueAlarmActive_updated() = with(repository) {
        isPastDueAlarmScheduled = true
        assertThat(isPastDueAlarmActive())
            .isTrue()
    }

    @Test
    fun test_cancelAllAlarms_base() = with(repository) {
        cancelAllAlarms()
        assertThat(isPastDueAlarmActive())
            .isFalse()
    }

    @Test
    fun test_cancelAllAlarms_updated() = with(repository) {
        isPastDueAlarmScheduled = true
        cancelAllAlarms()
        assertThat(isPastDueAlarmActive())
            .isFalse()
    }

    @Test
    fun test_schedulePastDueAlarm_base() = with(repository) {
        schedulePastDueAlarm()
        assertThat(isPastDueAlarmActive())
            .isTrue()
    }
}