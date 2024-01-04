package com.ivangarzab.carrus.util.managers

import android.app.Notification
import android.app.NotificationManager
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.test_data.STRING_TEST
import org.junit.Before
import org.junit.Test

class NotificationControllerTest {

    private lateinit var controller: NotificationController

    @Before
    fun setup() {
        controller = TestNotificationController()
    }

    @Test
    fun test_getNotificationManager() {
        val result = controller.getNotificationManager()
        assertThat(result)
            .isInstanceOf(NotificationManager::class.java)
    }

    @Test
    fun test_getProgressBarNotification() {
        val result = controller.getProgressBarNotification(STRING_TEST, STRING_TEST, false)
        assertThat(result)
            .isInstanceOf(Notification::class.java)
    }

    @Test
    fun test_getReminderNotification() {
        val result = controller.getReminderNotification(NotificationData(STRING_TEST, STRING_TEST))
        assertThat(result)
            .isInstanceOf(Notification::class.java)
    }
}