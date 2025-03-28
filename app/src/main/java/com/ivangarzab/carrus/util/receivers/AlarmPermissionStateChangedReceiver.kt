package com.ivangarzab.carrus.util.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmPermissionStateChangedReceiver(
    val onStateChanged: (isGranted: Boolean) -> Unit
) : BroadcastReceiver() {

//    val analytics: Analytics by inject(Analytics::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                Timber.d("Received alarm permission state changed broadcast")
                onStateChanged(true) // Is this a safe assumption?
//                analytics.logAlarmsPermissionResult(true)
                //TODO: Continue listening until we're ready to exit the app
                context?.unregisterReceiver(this)
            }
        }
    }
}