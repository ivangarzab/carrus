package com.ivangarzab.carrus.data.repositories

import android.app.AlarmManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.states.AlarmSettingsState
import com.ivangarzab.carrus.util.extensions.isAbleToScheduleExactAlarms
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.carrus.util.managers.Preferences
import com.ivangarzab.carrus.util.receivers.AlarmPermissionStateChangedReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmSettingsRepositoryImpl(
    context: Context,
    private val prefs: Preferences,
    private val analytics: Analytics
) : AlarmSettingsRepository {

    private val _alarmSettingsFlow = MutableStateFlow(AlarmSettingsState())
    override val alarmSettingsFlow: MutableStateFlow<AlarmSettingsState>
        get() = _alarmSettingsFlow

    init {
        // Make sure the alarm permissions are granted for devices running Android 11 =<
        setIsAlarmPermissionGranted(
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .isAbleToScheduleExactAlarms()
        )
        // Extract initial data
        toggleAlarmFeature(isAlarmFeatureOn())
        setAlarmTime(getAlarmTime())
        setAlarmFrequency(getAlarmFrequency())
    }

    private fun updateAlarmSettingsFlow(data: AlarmSettingsState) {
        _alarmSettingsFlow.value = data
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun listenForAlarmPermissionChanges(context: Context) {
        ContextCompat.registerReceiver(
            context,
            AlarmPermissionStateChangedReceiver {
                analytics.logAlarmsPermissionResult(it)
                setIsAlarmPermissionGranted(it)
            },
            IntentFilter(
                AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
            ),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun observeAlarmSettingsData(): Flow<AlarmSettingsState> = alarmSettingsFlow.asStateFlow()

    override fun isAlarmPermissionGranted(): Boolean = alarmSettingsFlow.value.isAlarmPermissionGranted

    override fun setIsAlarmPermissionGranted(granted: Boolean) {
        Timber.v("Setting alarm permission ${if (granted) "granted" else "not granted"}")
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmPermissionGranted = granted
        ))
    }

    override fun isAlarmFeatureOn(): Boolean = prefs.isAlarmFeatureOn

    override fun toggleAlarmFeature(isEnabled: Boolean) {
        Timber.v("Toggling alarm feature ${if (isEnabled) "ON" else "OFF"}")
        prefs.isAlarmFeatureOn = isEnabled
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmFeatureEnabled = isEnabled
        ))
    }

    override fun getAlarmTime(): Int = prefs.alarmPastDueTime ?: DEFAULT_ALARM_TIME

    /**
     * Set a new alarm time, based on a 24-hour clock reference.  I.e., parameter [alarmTime]
     * should be a number between [0 - 23].
     */
    override fun setAlarmTime(alarmTime: Int) {
        Timber.v("Setting alarm time to $alarmTime")
        prefs.alarmPastDueTime = alarmTime
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            alarmTime = AlarmTime(alarmTime)
        ))
    }

    override fun getAlarmFrequency(): AlarmFrequency = prefs.alarmFrequency

    override fun setAlarmFrequency(frequency: AlarmFrequency) {
        Timber.v("Setting alarm frequency to ${frequency.value}")
        prefs.alarmFrequency = frequency
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            frequency = frequency
        ))
    }
}
const val DEFAULT_ALARM_TIME: Int = 7
