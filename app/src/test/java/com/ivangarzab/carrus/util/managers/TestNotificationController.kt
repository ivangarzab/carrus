package com.ivangarzab.carrus.util.managers

import android.app.Notification
import android.app.NotificationManager
import com.ivangarzab.test_data.data.TestNotification
import io.mockk.mockk

class TestNotificationController : NotificationController {
    override fun getNotificationManager(): NotificationManager {
        return mockk()
    }

    override fun getProgressBarNotification(
        title: String,
        body: String,
        isLoading: Boolean
    ): Notification {
        return TestNotification()
    }

    override fun getReminderNotification(data: NotificationData): Notification {
        return TestNotification()
    }
}