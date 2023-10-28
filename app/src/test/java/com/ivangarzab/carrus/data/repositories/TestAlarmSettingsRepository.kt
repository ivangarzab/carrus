package com.ivangarzab.carrus.data.repositories

import android.content.Context
import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AlarmSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

/**
 * Created by Ivan Garza Bermea.
 */
class TestAlarmSettingsRepository : AlarmSettingsRepository {

    @VisibleForTesting
    var _alarmSettingsFlow = MutableStateFlow(AlarmSettingsState())
    override val alarmSettingsFlow: StateFlow<AlarmSettingsState>
        get() = _alarmSettingsFlow

    @VisibleForTesting
    var _isListeningForAlarmPermissionChanges: Boolean = false

    private fun updateAlarmSettingsFlow(update: AlarmSettingsState) = runBlocking {
        _alarmSettingsFlow.value = update
    }

    override fun listenForAlarmPermissionChanges(context: Context) {
        this._isListeningForAlarmPermissionChanges = true
    }

    override fun observeAlarmSettingsData(): Flow<AlarmSettingsState> {
        return this.alarmSettingsFlow
    }

    override fun isAlarmPermissionGranted(): Boolean {
        return _alarmSettingsFlow.value.isAlarmPermissionGranted
    }

    override fun setIsAlarmPermissionGranted(granted: Boolean) {
        _alarmSettingsFlow.value = alarmSettingsFlow.value.copy(
            isAlarmPermissionGranted = granted
        )
    }

    override fun isAlarmFeatureOn(): Boolean {
        return _alarmSettingsFlow.value.isAlarmFeatureEnabled
    }

    override fun toggleAlarmFeature(isEnabled: Boolean) {
        _alarmSettingsFlow.value = alarmSettingsFlow.value.copy(
            isAlarmFeatureEnabled = isEnabled
        )
    }

    override fun getAlarmTime(): Int {
        return _alarmSettingsFlow.value.alarmTime.getTime(TimeFormat.HR24)
    }

    override fun setAlarmTime(alarmTime: Int) {
        _alarmSettingsFlow.value = alarmSettingsFlow.value.copy(
            alarmTime = AlarmTime(alarmTime)
        )
    }

    override fun getAlarmFrequency(): AlarmFrequency {
        return _alarmSettingsFlow.value.frequency
    }

    override fun setAlarmFrequency(frequency: AlarmFrequency) {
        _alarmSettingsFlow.value = alarmSettingsFlow.value.copy(
            frequency = frequency
        )
    }
}