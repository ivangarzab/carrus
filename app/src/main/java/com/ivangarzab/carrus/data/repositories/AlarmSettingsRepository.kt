package com.ivangarzab.carrus.data.repositories

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.extensions.isAbleToScheduleExactAlarms
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class AlarmSettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    data class AlarmSettingsState(
        val isAlarmPermissionGranted: Boolean = false,
        val isAlarmFeatureEnabled: Boolean = false,
        val alarmTime: String = "",
        val frequency: AlarmFrequency = AlarmFrequency.DAILY
    )

    private val alarmSettingsFlow = MutableStateFlow(AlarmSettingsState())

    init {
        // Make sure the alarm permissions are granted for devices running Android 11 =<
        setIsAlarmPermissionGranted(
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .isAbleToScheduleExactAlarms()
        )
        // Listen for alarm permission changes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.registerReceiver(
                context,
                AlarmPermissionStateChangedReceiver(),
                IntentFilter(
                    AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
                ),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        // Extract initial data
        toggleAlarmFeature(isAlarmFeatureOn())
        setAlarmTime(getAlarmTime())
        setAlarmFrequency(getAlarmFrequency())
    }

    private fun updateAlarmSettingsFlow(data: AlarmSettingsState) {
        alarmSettingsFlow.value = data
    }

    fun observeAlarmSettingsData(): Flow<AlarmSettingsState> = alarmSettingsFlow.asStateFlow()

    fun isAlarmPermissionGranted(): Boolean = alarmSettingsFlow.value.isAlarmPermissionGranted

    fun setIsAlarmPermissionGranted(granted: Boolean) {
        Timber.v("Setting alarm permission ${if (granted) "granted" else "not granted"}")
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmPermissionGranted = granted
        ))
    }

    fun isAlarmFeatureOn(): Boolean = prefs.isAlarmFeatureOn

    fun toggleAlarmFeature(isEnabled: Boolean) {
        Timber.v("Toggling alarm feature ${if (isEnabled) "ON" else "OFF"}")
        prefs.isAlarmFeatureOn = isEnabled
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmFeatureEnabled = isEnabled
        ))
    }

    fun getAlarmTime(): Int = prefs.alarmPastDueTime ?: DEFAULT_ALARM_TIME

    fun setAlarmTime(alarmTime: Int) {
        Timber.v("Setting alarm time to $alarmTime")
        prefs.alarmPastDueTime = alarmTime
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            alarmTime = alarmTime.toString()
        ))
    }

    fun getAlarmFrequency(): AlarmFrequency = prefs.alarmFrequency

    fun setAlarmFrequency(frequency: AlarmFrequency) {
        Timber.v("Setting alarm frequency to ${frequency.value}")
        prefs.alarmFrequency = frequency
        updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            frequency = frequency
        ))
    }

    inner class AlarmPermissionStateChangedReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                    Timber.d("Received alarm permission state changed broadcast")
                    setIsAlarmPermissionGranted(true)
                    //TODO: Continue listening until we're ready to exit the app
                    context?.unregisterReceiver(this)
                }
            }
        }
    }
}

const val DEFAULT_ALARM_TIME:Int = 7