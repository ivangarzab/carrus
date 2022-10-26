package com.ivangarzab.carbud.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivangarzab.carbud.alarms
import com.ivangarzab.carbud.carRepository
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.data.isPastDue
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.AlarmScheduler
import com.ivangarzab.carbud.util.NotificationController
import com.ivangarzab.carbud.util.NotificationData
import com.ivangarzab.carbud.util.extensions.getFormattedDate
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var notificationController: NotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        this.context = context
        this.notificationController = NotificationController(context)

        intent?.let {
            Timber.d("We got an alarm intent with action: ${it.action}")
            when (it.action) {
                INTENT_ACTION_BOOT_COMPLETED -> handleDeviceRebootAction()
                AlarmScheduler.INTENT_ACTION_ALARM_PAST_DUE -> handlePastDueAlarmIntent()
                else -> "Unable to recognize alarm intent action"
            }
        }
    }

    private fun handleDeviceRebootAction() {
        if (prefs.isAlarmPastDueActive) {
            Timber.d("Rescheduling 'PastDue' alarm")
            alarms.schedulePastDueAlarm()
        } else {
            Timber.w("Unable to find 'PastDue' alarm Intent")
        }
    }

    private fun handlePastDueAlarmIntent() {
        carRepository.fetchCarData()?.let { car ->
            processPastRepairDatesList(
                filterPastDueServices(car.services)
            )
        } ?: Timber.v("No past due dates found for today")
    }

    private fun processPastRepairDatesList(pastDueServiceList: List<Service>) {
        pastDueServiceList.forEach {
            Timber.v("Service '${it.name}' is past due with date: ${it.dueDate.getFormattedDate()}")
        }
        // TODO: Schedule Notification based on the Setting's constraints
        notificationController.notificationManager.notify(
            NOTIFICATION_ID_PAST_DUE,
            notificationController.getReminderNotification(
                NotificationData(
                    title = "A Service is due!",
                    body = "Make sure to take care of that past due service."
                )
            )
        )
    }

    private fun filterPastDueServices(serviceList: List<Service>): List<Service> =
        serviceList.filter { it.isPastDue() }

    companion object {
        private const val INTENT_ACTION_BOOT_COMPLETED: String = "android.intent.action.BOOT_COMPLETED"
        private const val NOTIFICATION_ID_PAST_DUE: Int = 300
    }
}