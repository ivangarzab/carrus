package com.ivangarzab.carbud.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.TAG

/**
 * Created by Ivan Garza Bermea.
 */
class NotificationController(
    val context: Context
) {
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        notificationManager.cancelAll() // TODO: Keep or discard?
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    fun getProgressBarNotification(
        title: String,
        body: String,
        isLoading: Boolean
    ): Notification {
        Log.d(TAG, "Showing progress bar notification: $isLoading")
        val channelId = createNotificationChannel("Reminders", "Service due date reminders.")
        return NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(title)
            setContentText(body)
            setSmallIcon(R.drawable.ic_mark_white)
            priority = NotificationCompat.PRIORITY_LOW
            if (isLoading) {
                setProgress(0, 0, true)
            } else {
                setProgress(0, 0, false)
            }
        }.build()
    }

    fun getReminderNotification(
        data: NotificationData
    ): Notification {
        val channelId = createNotificationChannel("Reminders", "Service due date reminders.")
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_mark_white)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentText(data.title)
            setContentText(data.body)
            setContentIntent(getActivityIntent())
        }.build()
    }

    fun getActivityIntent(): PendingIntent {
        return Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 1, it, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}

data class NotificationData(
    val title: String,
    val body: String,
    val contentIntent: PendingIntent? = null,
    val dismissIntent: PendingIntent? = null
)