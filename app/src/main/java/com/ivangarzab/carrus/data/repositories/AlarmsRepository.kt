package com.ivangarzab.carrus.data.repositories

import android.content.Context
import com.ivangarzab.carrus.data.alarm.Alarm
import com.ivangarzab.carrus.data.alarm.AlarmSchedulingData
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.AlarmScheduler
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

    private fun scheduleAlarm(type: Alarm, force: Boolean = false) {
        if (alarmSettingsRepository.isAlarmFeatureOn()) {
            if (force.not() && getIsAlarmActiveFlag(type)) {
                Timber.w("${type.name} alarm is already scheduled")
                //return // skip dupes..?
                cancelAlarm(type)
            }
            scheduler.scheduleAlarm(getAlarmSchedulingData(type)) {
                toggleAlarmActiveFlag(type, it)
                when (it) {
                    true -> Timber.d("${type.name} alarm scheduled successfully")
                    false -> Timber.w("Unable to schedule ${type.name} alarm due to unknown reasons")
                }
            }
        } else {
            Timber.w("Unable to schedule ${type.name} alarm due to missing permissions")
        }
    }

    private fun cancelAlarm(type: Alarm) {
        Timber.d("Canceling ${type.name} alarm")
        scheduler.cancelAlarm(getAlarmSchedulingData(type)) {
            toggleAlarmActiveFlag(type, it)
            when (it) {
                true -> Timber.d("${type.name} alarm cancelled successfully")
                false -> Timber.w("Unable to cancel ${type.name} alarm")
            }
        }
    }

    private fun getAlarmSchedulingData(type: Alarm): AlarmSchedulingData =
        AlarmSchedulingData(
            type = type,
            frequency = alarmSettingsRepository.getAlarmFrequency(),
            triggerTime = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MINUTE, 0)
                set(
                    Calendar.HOUR_OF_DAY,
                    alarmSettingsRepository.getAlarmTime()
                )
            }.timeInMillis,
            intentRequestCode = type.requestCode,
            intentAction = type.intentAction
        )

    private fun getIsAlarmActiveFlag(type: Alarm): Boolean = when (type) {
        Alarm.PAST_DUE -> prefs.isAlarmPastDueActive
        else -> {
            Timber.v("Defaulting to 'false' for non-critical alarm flag queries")
            false
        }
    }

    private fun toggleAlarmActiveFlag(type: Alarm, isActive: Boolean) = when (type) {
        Alarm.PAST_DUE -> prefs.isAlarmPastDueActive = isActive
        else -> Timber.v("Skipping non-critical alarm toggles")
    }

    fun cancelAllAlarms() {
        //TODO: Check for all active alarms and cancel them
    }

    fun isPastDueAlarmActive(): Boolean = getIsAlarmActiveFlag(Alarm.PAST_DUE)

    fun schedulePastDueAlarm(force: Boolean = false) = with(Alarm.PAST_DUE) {
        Timber.d("Got request to schedule ${this.name} alarm")
        scheduleAlarm(type = this, force = force)
    }
}