package com.ivangarzab.carrus.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.receivers.AlarmBroadcastReceiver
import com.ivangarzab.carrus.util.alarms.AlarmSchedulingData
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmScheduler(
    context: Context
) {
    private val weakContext: WeakReference<Context> = WeakReference(context)

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(
        alarmData: AlarmSchedulingData,
        onDone: (Boolean) -> Unit
    ) = getAlarmPendingIntent(alarmData)?.let {
        Timber.d("Scheduling ${alarmData.type.name} alarm")
        setAlarmBroadcastReceiverEnableState(true)
        scheduleTestAlarm(it) //scheduleDefaultDailyAlarm(it)
        onDone(true)
    } ?: onDone(false)


   /* fun schedulePastDueAlarm(force: Boolean = false) {
        *//*if (force.not() && prefs.isAlarmPastDueActive) {
            Timber.v("'PastDueService' alarm is already scheduled")
//            return // skip dupes
            cancelPastDueAlarm()
        }*//*

//        getPastDueAlarmPendingIntent()?.let {
            if (alarmManager.isAbleToScheduleExactAlarms()) {
                prefs.isAlarmPastDueActive = true
                Timber.d("Scheduling 'PastDue' alarm")
                setAlarmBroadcastReceiverEnableState(true)
                scheduleDefaultDailyAlarm(it)
            }*//* else {
                Timber.w("Unable to schedule 'PastDue' alarm due to missing permissions")
            }*//*
        //} ?: Timber.w("Unable to schedule 'PastDue' alarm for an unknown reason")
    }*/


    fun cancelAlarm(
        alarmData: AlarmSchedulingData,
        onDone: (Boolean) -> Unit
    ) {
        setAlarmBroadcastReceiverEnableState(false)
        getAlarmPendingIntent(alarmData)?.let {
            alarmManager.cancel(it)
            it.cancel()
            onDone(true)
        } ?: onDone(false)
    }
 /*   fun cancelPastDueAlarm() {
        setAlarmBroadcastReceiverEnableState(false)
        getPastDueAlarmPendingIntent()?.let {
            alarmManager.cancel(it)
            it.cancel()
        } ?: Timber.w("Unable to cancel 'PastDue' alarm")
        prefs.isAlarmPastDueActive = false
    }*/


    private fun getAlarmPendingIntent(data: AlarmSchedulingData): PendingIntent? {
        return weakContext.get()?.let {
            Intent(it, AlarmBroadcastReceiver::class.java).let { intent ->
                intent.action = data.intentAction
                PendingIntent.getBroadcast(
                    it,
                    data.intentRequestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }
    /*private fun getPastDueAlarmPendingIntent(): PendingIntent? {
        return weakContext.get()?.let {
            Intent(it, AlarmBroadcastReceiver::class.java).let { intent ->
                intent.action = INTENT_ACTION_ALARM_PAST_DUE
                PendingIntent.getBroadcast(
                    it,
                    REQUEST_CODE_ALARM_PAST_DUE,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }*/

    private fun scheduleDefaultDailyAlarm(alarmIntent: PendingIntent) {
        val alarmTime: Int = prefs.alarmPastDueTime ?: DEFAULT_ALARM_TIME // 7am is the default
        alarmManager.setRepeating(
            AlarmManager.RTC,
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MINUTE, 0)
                set(
                    Calendar.HOUR_OF_DAY,
                    alarmTime
                )
            }.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        Timber.d("Scheduled daily alarm at $alarmTime")
    }

    private fun scheduleTestAlarm(alarmIntent: PendingIntent) {
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(15), // 15 seconds from now
            TimeUnit.MINUTES.toMillis(1),
            alarmIntent
        )
        Timber.d("Scheduled test alarm 15 seconds from now with an interval of a minute")
    }

    /**
     * Enable the [AlarmBroadcastReceiver] to start or stop receiving alarm broadcast.
     */
    private fun setAlarmBroadcastReceiverEnableState(enabled: Boolean) {
        weakContext.get()?.let { context ->
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, AlarmBroadcastReceiver::class.java),
                when (enabled) {
                    true -> PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    else -> PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                },
                PackageManager.DONT_KILL_APP
            )
        }
    }

    companion object {
        const val REQUEST_CODE_ALARM_PAST_DUE: Int = 200
        const val INTENT_ACTION_ALARM_PAST_DUE: String = "alarm-past-due"
    }
}