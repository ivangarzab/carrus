package com.ivangarzab.carbud.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.ivangarzab.carbud.PastDueService
import com.ivangarzab.carbud.TAG
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.AlarmScheduler.Companion.ACTION_CODE_ALARM_PAST_DUE
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
            SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(1), // a minute from now
            TimeUnit.MINUTES.toMillis(3), // every 3 minutes
            alarmIntent
        )
        Log.d(TAG, "Scheduled test alarm a minute from now with an interval of 3 minutes")
    }

    fun cancelAlarm(intent: PendingIntent) {
        alarmManager.cancel(intent)
    }

    companion object {
        const val REQUEST_CODE_ALARM_PAST_DUE: Int = 100
        const val ACTION_CODE_ALARM_PAST_DUE: String = "alarm-past-due"
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            Log.d(TAG, "We got an alarm intent: $it")
            when (it.action) {
                ACTION_CODE_ALARM_PAST_DUE -> context?.startService(
                    Intent(context, PastDueService::class.java)
                )
                else -> "Unable to recognize alarm intent action"
            }
        }
    }
}