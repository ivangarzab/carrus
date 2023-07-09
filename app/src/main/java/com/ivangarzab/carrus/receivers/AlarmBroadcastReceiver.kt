package com.ivangarzab.carrus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.alarm.Alarm
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.util.AlarmScheduler
import com.ivangarzab.carrus.util.NotificationController
import com.ivangarzab.carrus.util.NotificationData
import com.ivangarzab.carrus.util.extensions.getFormattedDate
import com.ivangarzab.carrus.util.extensions.isPastDue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class AlarmBroadcastReceiver @Inject constructor() : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var notificationController: NotificationController

    @Inject lateinit var carRepository: CarRepository
    @Inject lateinit var alarmsRepository: AlarmsRepository
    @Inject lateinit var alarmSettingsRepository: AlarmSettingsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        this.context = context
        this.notificationController = NotificationController(context)

        intent?.let {
            Timber.d("We got an alarm intent with action: ${it.action}")
            when (it.action) {
                INTENT_ACTION_BOOT_COMPLETED -> handleDeviceRebootAction()
                Alarm.PAST_DUE.intentAction -> handlePastDueAlarmIntent()
                else -> "Unable to recognize alarm intent action"
            }
        }
    }

    private fun handleDeviceRebootAction() = with(alarmsRepository) {
        //TODO: Delegate to the alarmsRepository
        if (alarmSettingsRepository.isAlarmFeatureOn() && isPastDueAlarmActive()) {
            Timber.d("Rescheduling PAST_DUE alarm")
            alarmsRepository.schedulePastDueAlarm()
        } else {
            Timber.w("Unable to find PAST_DUE alarm Intent")
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
        when (pastDueServiceList.isEmpty()) {
            true -> Timber.v("There's no past due services")
            false -> {
                pastDueServiceList.forEach {
                    Timber.v("Service '${it.name}' is past due with date: ${it.dueDate.getFormattedDate()}")
                }
                // TODO: Schedule Notification based on the Setting's constraints
                notificationController.notificationManager.notify(
                    NOTIFICATION_ID_PAST_DUE,
                    notificationController.getReminderNotification(
                        NotificationData(
                            title = context.getString(R.string.notification_past_due_title),
                            body = when (pastDueServiceList.size > 1) {
                                true -> context.getString(R.string.notification_past_due_service_multiple)
                                false -> context.getString(
                                    R.string.notification_past_due_service_one,
                                    pastDueServiceList[0].name
                                )
                            }
                        )
                    )
                )
            }
        }
    }

    private fun filterPastDueServices(serviceList: List<Service>): List<Service> =
        serviceList.filter { it.isPastDue() }

    companion object {
        private const val INTENT_ACTION_BOOT_COMPLETED: String = "android.intent.action.BOOT_COMPLETED"
        private const val NOTIFICATION_ID_PAST_DUE: Int = 300
    }
}