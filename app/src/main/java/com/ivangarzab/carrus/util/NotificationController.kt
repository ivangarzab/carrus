package com.ivangarzab.carrus.util

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ivangarzab.carrus.MainActivity
import com.ivangarzab.carrus.MainActivity.Companion.REQUEST_CODE
import com.ivangarzab.carrus.R
import timber.log.Timber

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
        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup(
                NOTIFICATION_GROUP_REMINDERS_ID,
                NOTIFICATION_GROUP_REMINDERS_NAME
        ))
        NotificationChannel(
            NOTIFICATION_CHANNEL_DUE_DATE_ID,
            NOTIFICATION_CHANNEL_DUE_DATE_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            group = NOTIFICATION_GROUP_REMINDERS_ID
            notificationManager.createNotificationChannel(this)
        }
    }

    fun getProgressBarNotification(
        title: String,
        body: String,
        isLoading: Boolean
    ): Notification {
        Timber.d("Showing progress bar notification: $isLoading")
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_DUE_DATE_ID).apply {
            setSmallIcon(NOTIFICATION_ICON_RES)
            priority = NotificationCompat.PRIORITY_LOW
            setContentTitle(title)
            setContentText(body)
            if (isLoading) {
                setProgress(0, 0, true)
            } else {
                setProgress(0, 0, false)
                setAutoCancel(true)
            }
        }.build()
    }

    fun getReminderNotification(
        data: NotificationData
    ): Notification {
        Timber.d("Showing reminder notification with data: $data")
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_DUE_DATE_ID).apply {
            setSmallIcon(NOTIFICATION_ICON_RES)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
            setContentTitle(data.title)
            setContentText(data.body)
            setContentIntent(data.contentIntent ?: getActivityIntent())
        }.build()
    }

    private fun getActivityIntent(): PendingIntent =
        Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(
                context,
                REQUEST_CODE,
                it,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    companion object {
        private const val NOTIFICATION_ICON_RES = R.drawable.ic_mark_black
        const val NOTIFICATION_GROUP_REMINDERS_ID = "notification-group-reminders"
        const val NOTIFICATION_GROUP_REMINDERS_NAME = "Reminders"
        const val NOTIFICATION_CHANNEL_DUE_DATE_ID = "notification-channel-past-due"
        const val NOTIFICATION_CHANNEL_DUE_DATE_NAME = "Past Due"
    }
}

data class NotificationData(
    val title: String,
    val body: String,
    val contentIntent: PendingIntent? = null,
    val dismissIntent: PendingIntent? = null
)