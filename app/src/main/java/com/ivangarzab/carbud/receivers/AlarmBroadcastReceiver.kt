package com.ivangarzab.carbud.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ivangarzab.carbud.TAG
import com.ivangarzab.carbud.carRepository
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.data.isPastDue
import com.ivangarzab.carbud.util.AlarmScheduler
import com.ivangarzab.carbud.util.NotificationController
import com.ivangarzab.carbud.util.NotificationData
import com.ivangarzab.carbud.util.extensions.getFormattedDate

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
            Log.d(TAG, "We got an alarm intent with action: ${it.action}")
            when (it.action) {
                AlarmScheduler.ACTION_CODE_ALARM_PAST_DUE -> checkDueDates()
                else -> "Unable to recognize alarm intent action"
            }
        }
    }

    private fun checkDueDates() {
        fetchCarData()?.let { car ->
            processPastRepairDatesList(
                filterPastDueServices(car.services)
            )
        } ?: Log.i(TAG, "No past due dates found for today")
    }

    private fun fetchCarData(): Car? = carRepository.getDefaultCar()

    private fun filterPastDueServices(serviceList: List<Service>): List<Service> =
        serviceList.filter { it.isPastDue() }

    private fun processPastRepairDatesList(pastDueServiceList: List<Service>) {
        pastDueServiceList.forEach {
            Log.v(TAG, "Service '${it.name}' is past due with date: ${it.dueDate.getFormattedDate()}")
        }
        // TODO: Schedule Notification based on the Setting's constraints
        notificationController.notificationManager.notify(
            600, notificationController.getReminderNotification(
                NotificationData(
                    title = "A Service is due!",
                    body = "Make sure to take care of that past due service."
                )
            )
        )
    }
}