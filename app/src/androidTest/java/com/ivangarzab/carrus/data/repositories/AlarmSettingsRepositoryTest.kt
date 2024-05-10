package com.ivangarzab.carrus.data.repositories

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.providers.DebugFlagProviderImpl
import com.ivangarzab.carrus.util.managers.Preferences
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test the contents of [AlarmSettingsRepository].
 *
 * Created by Ivan Garza Bermea.
 */
class AlarmSettingsRepositoryTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    private val prefs: Preferences = Preferences(
        context,
        DebugFlagProviderImpl().apply { forceDebug = true }
    )

    private val repository = AlarmSettingsRepositoryImpl(context, prefs)

    @Before
    fun setup() {
        prefs.isAlarmFeatureOn = true
        prefs.alarmPastDueTime = null
        prefs.alarmFrequency = AlarmFrequency.DAILY
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
        prefs.isAlarmFeatureOn = true
        assertThat(isAlarmFeatureOn())
            .isTrue()
    }

    @Test
    fun test_isAlarmFeatureOn_false() = with(repository) {
        prefs.isAlarmFeatureOn = false
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
        prefs.alarmPastDueTime = TEST_ALARM_TIME
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
        prefs.alarmFrequency = AlarmFrequency.WEEKLY
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