package com.ivangarzab.carrus.util.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivangarzab.carrus.util.managers.Analytics
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmPermissionStateChangedReceiver(
    val onStateChanged: (isGranted: Boolean) -> Unit
) : BroadcastReceiver() {

    @Inject
    lateinit var analytics: Analytics

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                Timber.d("Received alarm permission state changed broadcast")
                onStateChanged(true) // Is this a safe assumption?
                analytics.logAlarmsPermissionResult(true)
                //TODO: Continue listening until we're ready to exit the app
                context?.unregisterReceiver(this)
            }
        }
    }
}