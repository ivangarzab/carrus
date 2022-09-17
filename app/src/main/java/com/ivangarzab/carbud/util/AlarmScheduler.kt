package com.ivangarzab.carbud.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.ivangarzab.carbud.*
import com.ivangarzab.carbud.receivers.AlarmBroadcastReceiver
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

    fun schedulePastDueServiceAlarm() {
        val context = if (weakContext.get() != null) {
            weakContext.get()
        } else {
            Log.w(TAG, "Unable to schedule 'PastDueService' alarm because of missing Context")
            return // we bail
        }

        prefs.pastDueAlarmIntent?.let {
            Log.v(TAG, "'PastDueService' alarm is already scheduled")
//            return // skip dupes
            cancelAlarm(it)
            prefs.pastDueAlarmIntent = null
        }

        Log.d(TAG, "Scheduling 'PastDueService' alarm")
        val alarmIntent: PendingIntent = Intent(context, AlarmBroadcastReceiver::class.java).let { intent ->
            intent.action = ACTION_CODE_ALARM_PAST_DUE
            PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ALARM_PAST_DUE,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        // persist for later deletion
        prefs.pastDueAlarmIntent = alarmIntent
        scheduleTestAlarm(alarmIntent)
    }

    private fun scheduleDefaultDailyAlarm(alarmIntent: PendingIntent) {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 9) // today at 9am
            }.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        Log.d(TAG, "Scheduled daily alarm at 9am")
    }

    private fun scheduleTestAlarm(alarmIntent: PendingIntent) {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(15), // 15 seconds from now
            TimeUnit.MINUTES.toMillis(1), // every minute
            alarmIntent
        )
        Log.d(TAG, "Scheduled test alarm 15 seconds from now with an interval of a minute")
    }

    fun cancelAlarm(intent: PendingIntent) {
        alarmManager.cancel(intent)
    }

    companion object {
        const val REQUEST_CODE_ALARM_PAST_DUE: Int = 100
        const val ACTION_CODE_ALARM_PAST_DUE: String = "alarm-past-due"
    }
}