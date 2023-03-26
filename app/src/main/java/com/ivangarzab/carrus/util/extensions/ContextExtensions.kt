package com.ivangarzab.carrus.util.extensions

import android.app.NotificationManager
import android.content.Context

/**
 * Created by Ivan Garza Bermea.
 */
fun Context.areNotificationsEnabled(): Boolean = (getSystemService(
    Context.NOTIFICATION_SERVICE
) as NotificationManager).areNotificationsEnabled()