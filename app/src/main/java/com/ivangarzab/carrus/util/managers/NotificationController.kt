package com.ivangarzab.carrus.util.managers

import android.app.Notification
import android.app.NotificationManager

interface NotificationController {
    fun getNotificationManager(): NotificationManager

    fun getProgressBarNotification(
        title: String,
        body: String,
        isLoading: Boolean
    ): Notification

    fun getReminderNotification(data: NotificationData): Notification
}