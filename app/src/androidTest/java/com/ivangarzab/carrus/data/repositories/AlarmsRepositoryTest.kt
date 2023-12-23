package com.ivangarzab.carrus.data.repositories

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.di.DebugFlagProviderImpl
import com.ivangarzab.carrus.util.managers.Preferences
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmsRepositoryTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    private val prefs: Preferences = Preferences(
        context,
        DebugFlagProviderImpl().apply { forceDebug = true }
    )

    private val repository = AlarmsRepositoryImpl(
        context = context,
        alarmSettingsRepository = AlarmSettingsRepositoryImpl(context, prefs),
        prefs = prefs
    )

    @Before
    fun setup() {
        prefs.isAlarmPastDueActive = false
        // alarmSettingsRepository defaults
        prefs.isAlarmFeatureOn = true
        prefs.alarmPastDueTime = null
        prefs.alarmFrequency = AlarmFrequency.DAILY
    }

    @Test
    fun test_isPastDueAlarmActive_base() = with(repository) {
        assertThat(isPastDueAlarmActive())
            .isFalse()
    }

    @Test
    fun test_isPastDueAlarmActive_true() = with(repository) {
        prefs.isAlarmPastDueActive = true
        assertThat(isPastDueAlarmActive())
            .isTrue()
    }

    @Test
    fun test_cancelAllAlarms() = with(repository) {
        cancelAllAlarms()
        assertThat(isPastDueAlarmActive())
            .isFalse()
    }
}