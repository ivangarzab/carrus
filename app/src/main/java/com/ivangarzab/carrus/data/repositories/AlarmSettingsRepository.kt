package com.ivangarzab.carrus.data.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.states.AlarmSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ivan Garza Bermea.
 */
interface AlarmSettingsRepository {

    val alarmSettingsFlow: StateFlow<AlarmSettingsState>

    @RequiresApi(Build.VERSION_CODES.S)
    fun listenForAlarmPermissionChanges(context: Context)
    fun observeAlarmSettingsData(): Flow<AlarmSettingsState>
    fun isAlarmPermissionGranted(): Boolean
    fun setIsAlarmPermissionGranted(granted: Boolean)
    fun isAlarmFeatureOn(): Boolean
    fun toggleAlarmFeature(isEnabled: Boolean)
    fun getAlarmTime(): Int
    fun setAlarmTime(alarmTime: Int)
    fun getAlarmFrequency(): AlarmFrequency
    fun setAlarmFrequency(frequency: AlarmFrequency)

}