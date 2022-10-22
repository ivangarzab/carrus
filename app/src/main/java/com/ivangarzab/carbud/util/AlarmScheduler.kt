package com.ivangarzab.carbud.util

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import com.ivangarzab.carbud.*
import com.ivangarzab.carbud.receivers.AlarmBroadcastReceiver
import com.ivangarzab.carbud.util.extensions.isAbleToScheduleExactAlarms
import java.lang.ref.WeakReference
import java.util.*
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

    fun schedulePastDueAlarm() {
        if (prefs.isAlarmPastDueActive) {
            Log.v(TAG, "'PastDueService' alarm is already scheduled")
//            return // skip dupes
            cancelPastDueAlarm()
        }

        getPastDueAlarmPendingIntent()?.let {
            if (alarmManager.isAbleToScheduleExactAlarms()) {
                prefs.isAlarmPastDueActive = true
                Log.d(TAG, "Scheduling 'PastDue' alarm")
                setAlarmBroadcastReceiverEnableState(true)
                scheduleDefaultDailyAlarm(it)
            } else {
                Log.w(TAG, "Unable to schedule 'PastDue' alarm due to missing permissions")
            }
        } ?: Log.w(TAG, "Unable to schedule 'PastDue' alarm for an unknown reason")
    }

    fun cancelPastDueAlarm() {
        setAlarmBroadcastReceiverEnableState(false)
        getPastDueAlarmPendingIntent()?.let {
            alarmManager.cancel(it)
            it.cancel()
        } ?: Log.w(TAG, "Unable to cancel 'PastDue' alarm")
        prefs.isAlarmPastDueActive = false
    }

    private fun getPastDueAlarmPendingIntent(): PendingIntent? {
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
    }

    private fun scheduleDefaultDailyAlarm(alarmIntent: PendingIntent) {
        alarmManager.setRepeating(
            AlarmManager.RTC,
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 7) // today at 7am
            }.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        Log.d(TAG, "Scheduled daily alarm at 7am")
    }

    private fun scheduleTestAlarm(alarmIntent: PendingIntent) {
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(15), // 15 seconds from now
            TimeUnit.MINUTES.toMillis(1),
            alarmIntent
        )
        Log.d(TAG, "Scheduled test alarm 15 seconds from now with an interval of a minute")
    }

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
        const val REQUEST_CODE_ALARM_PAST_DUE: Int = 100
        const val INTENT_ACTION_ALARM_PAST_DUE: String = "alarm-past-due"
    }
}