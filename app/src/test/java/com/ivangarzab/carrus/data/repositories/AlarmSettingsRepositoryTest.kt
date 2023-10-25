package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmSettingsRepositoryTest {

    private lateinit var repository: TestAlarmSettingsRepository

    @Before
    fun setup() {
        repository = TestAlarmSettingsRepository()
    }

    @Test
    fun test_isAlarmPermissionGranted_base() = with(repository) {
        assertThat(isAlarmPermissionGranted())
            .isTrue()
    }

    @Test
    fun test_isAlarmFeatureOn_base() = with(repository) {
        assertThat(isAlarmFeatureOn())
            .isTrue()
    }

    @Test
    fun test_isAlarmFeatureOn_true() = with(repository) {
        _isAlarmFeatureOn = true
        assertThat(isAlarmFeatureOn())
            .isTrue()
    }

    @Test
    fun test_isAlarmFeatureOn_false() = with(repository) {
        _isAlarmFeatureOn = false
        assertThat(isAlarmFeatureOn())
            .isFalse()
    }

    @Test
    fun test_getAlarmTime_base() = with(repository) {
        assertThat(getAlarmTime())
            .isEqualTo(DEFAULT_ALARM_TIME)
    }

    @Test
    fun test_getAlarmTime_non_default() = with(repository) {
        _alarmTime = TEST_ALARM_TIME
        assertThat(getAlarmTime())
            .isEqualTo(TEST_ALARM_TIME)
    }

    @Test
    fun test_setAlarmTime_base() = with(repository) {
        setAlarmTime(TEST_ALARM_TIME)
        assertThat(getAlarmTime())
            .isEqualTo(TEST_ALARM_TIME)
    }

    @Test
    fun test_getAlarmFrequency_base() = with(repository) {
        assertThat(getAlarmFrequency())
            .isSameInstanceAs(AlarmFrequency.DAILY)
    }

    @Test
    fun test_getAlarmFrequency_weekly() = with(repository) {
        _alarmFrequency = AlarmFrequency.WEEKLY
        assertThat(getAlarmFrequency())
            .isSameInstanceAs(AlarmFrequency.WEEKLY)
    }

    @Test
    fun test_setAlarmFrequency_weekly() = with(repository) {
        setAlarmFrequency(AlarmFrequency.WEEKLY)
        assertThat(getAlarmFrequency())
            .isSameInstanceAs(AlarmFrequency.WEEKLY)
    }

    @Test
    fun test_setAlarmFrequency_otherDay() = with(repository) {
        setAlarmFrequency(AlarmFrequency.OTHER_DAY)
        assertThat(getAlarmFrequency())
            .isSameInstanceAs(AlarmFrequency.OTHER_DAY)
    }

    companion object {
        private const val TEST_ALARM_TIME: Int = 6
    }
}