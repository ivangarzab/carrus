package com.ivangarzab.carrus.data.repositories

import android.content.Context
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.AlarmScheduler
import com.ivangarzab.carrus.util.alarms.AlarmSchedulingData
import com.ivangarzab.carrus.util.alarms.AlarmType
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

/**
 * The purpose of this file is to hold and manage information about particular alarms, as well as
 * scheduling new alarms whenever it is required.
 *
 * Created by Ivan Garza Bermea.
 */
class AlarmsRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val alarmSettingsRepository: AlarmSettingsRepository
) {

    private val scheduler = AlarmScheduler(context)

    init {
        if (alarmSettingsRepository.isAlarmFeatureOn().not()) {
            cancelAllAlarms()
        }
    }

    private fun scheduleAlarm(type: AlarmType, force: Boolean = false) {
        if (alarmSettingsRepository.isAlarmFeatureOn()) {
            //TODO: FIX isPastDueAlarmActive() being hardcoded
            if (force.not() && isPastDueAlarmActive()) {
                Timber.w("${type.name} alarm is already scheduled")
                //return // skip dupes..?
                cancelAlarm(type)
            }
            scheduler.scheduleAlarm(getAlarmSchedulingData(type)) {
                when (it) {
                    true -> Timber.d("${type.name} alarm scheduled successfully")
                    false -> Timber.w("Unable to schedule ${type.name} alarm due to unknown reasons")
                } //TODO: Update prefs
            }
        } else {
            Timber.w("Unable to schedule ${type.name} alarm due to missing permissions")
        }
    }

    private fun cancelAlarm(type: AlarmType) {
        Timber.d("Canceling ${type.name} alarm")
        scheduler.cancelAlarm(getAlarmSchedulingData(type)) {
            when (it) {
                true -> Timber.d("${type.name} alarm cancelled successfully")
                false -> Timber.w("Unable to cancel ${type.name} alarm")
            } //TODO: Update prefs
        }
    }

    fun cancelAllAlarms() {
        //TODO: Check for all active alarms and cancel them
    }

    private fun getAlarmSchedulingData(type: AlarmType): AlarmSchedulingData =
        type.getSchedulingData(
            frequency = alarmSettingsRepository.getAlarmFrequency(),
            alarmTime = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MINUTE, 0)
                set(
                    Calendar.HOUR_OF_DAY,
                    alarmSettingsRepository.getAlarmTime()
                )
            }.timeInMillis
        )

    private fun isPastDueAlarmActive(): Boolean = prefs.isAlarmPastDueActive

    fun schedulePastDueAlarm(force: Boolean = false) = with(AlarmType.PAST_DUE) {
        Timber.d("Got request to schedule ${this.name} alarm")
        scheduleAlarm(type = this, force = force)
    }

    //TODO: May not be needed
    private fun togglePastDueAlarmActive(isActive: Boolean) {
        Timber.v("Toggling alarm feature ${if (isActive) "ON" else "OFF"}")
        prefs.isAlarmPastDueActive = isActive
        /*updateAlarmSettingsFlow(alarmSettingsFlow.value.copy(
            isAlarmActive = isActive
        ))*/
    }
}