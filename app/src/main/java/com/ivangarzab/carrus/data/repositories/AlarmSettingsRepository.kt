package com.ivangarzab.carrus.data.repositories

import android.app.AlarmManager
import android.content.Context
import com.ivangarzab.carrus.data.AlarmFrequency
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.extensions.isAbleToScheduleExactAlarms
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmSettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    data class AlarmSettingsState(
        val isAlarmFeatureEnabled: Boolean = false,
        val isAlarmActive: Boolean = false,
        val alarmTime: String = "",
        val frequency: AlarmFrequency = AlarmFrequency.DAILY
    )

    init {
        // Make sure the alarm permissions are granted for devices running Android 11 =<
        toggleAlarmFeature(
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .isAbleToScheduleExactAlarms()
        )
    }

    private val alarmSettingsFlow = MutableStateFlow(AlarmSettingsState())

    private fun updateAlarmSettingsFlow(data: AlarmSettingsState) {
        alarmSettingsFlow.value = data
    }

    fun observeAlarmSettingsData(): Flow<AlarmSettingsState> = alarmSettingsFlow.asStateFlow()

    fun isAlarmFeatureOn(): Boolean = prefs.isAlarmFeatureOn

    fun toggleAlarmFeature(isEnabled: Boolean) {
        Timber.v("Toggling alarm feature ${if (isEnabled) "ON" else "OFF"}")
        prefs.isAlarmFeatureOn = isEnabled
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmFeatureEnabled = isEnabled
        ))
    }

    fun getAlarmTime(): Int = prefs.alarmPastDueTime ?: DEFAULT_ALARM_TIME

    fun setAlarmTime(value: String) = value.toInt().let { alarmTime ->
        Timber.v("Setting alarm time to $alarmTime")
        prefs.alarmPastDueTime = alarmTime
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            alarmTime = value
        ))
    }

    fun setAlarmFrequency(frequency: AlarmFrequency) {
        Timber.v("Setting alarm frequency to ${frequency.value}")
        prefs.alarmFrequency = frequency
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            frequency = frequency
        ))
    }

    fun isPastDueAlarmActive(): Boolean = prefs.isAlarmPastDueActive

    fun togglePastDueAlarmActive(isActive: Boolean) {
        Timber.v("Toggling alarm feature ${if (isActive) "ON" else "OFF"}")
        prefs.isAlarmPastDueActive = isActive
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmActive = isActive
        ))
    }
}

const val DEFAULT_ALARM_TIME:Int = 7