package com.ivangarzab.carrus.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import com.ivangarzab.carrus.data.alarm.AlarmSchedulingData
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.receivers.AlarmBroadcastReceiver
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
        scheduleDefaultDailyAlarm(it)
        onDone(true)
    } ?: onDone(false)

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
}