package com.ivangarzab.carrus.data.repositories

import android.content.Context
import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.states.AlarmSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ivan Garza Bermea.
 */
class TestAlarmSettingsRepository : AlarmSettingsRepository {

    private val _alarmSettingsFlow = MutableStateFlow(AlarmSettingsState())
    override val alarmSettingsFlow: StateFlow<AlarmSettingsState>
        get() = _alarmSettingsFlow

    @VisibleForTesting
    var _isListeningForAlarmPermissionChanges: Boolean = false
    @VisibleForTesting
    var _isAlarmPermissionGranted: Boolean = true
    @VisibleForTesting
    var _isAlarmFeatureOn: Boolean = true
    @VisibleForTesting
    var _alarmTime: Int = DEFAULT_ALARM_TIME
    @VisibleForTesting
    var _alarmFrequency: AlarmFrequency = AlarmFrequency.DAILY

    override fun listenForAlarmPermissionChanges(context: Context) {
        this._isListeningForAlarmPermissionChanges = true
    }

    override fun observeAlarmSettingsData(): Flow<AlarmSettingsState> {
        return this.alarmSettingsFlow
    }

    override fun isAlarmPermissionGranted(): Boolean {
        return this._isAlarmPermissionGranted
    }

    override fun setIsAlarmPermissionGranted(granted: Boolean) {
        this._isAlarmPermissionGranted = granted
    }

    override fun isAlarmFeatureOn(): Boolean {
        return this._isAlarmFeatureOn
    }

    override fun toggleAlarmFeature(isEnabled: Boolean) {
        this._isAlarmFeatureOn = isEnabled
    }

    override fun getAlarmTime(): Int {
        return this._alarmTime
    }

    override fun setAlarmTime(alarmTime: Int) {
        this._alarmTime = alarmTime
    }

    override fun getAlarmFrequency(): AlarmFrequency {
        return this._alarmFrequency
    }

    override fun setAlarmFrequency(frequency: AlarmFrequency) {
        this._alarmFrequency = frequency
    }
}