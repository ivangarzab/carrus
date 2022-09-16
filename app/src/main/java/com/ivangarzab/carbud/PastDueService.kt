package com.ivangarzab.carbud

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.isPastDue
import com.ivangarzab.carbud.data.Service as CarService

/**
 * Created by Ivan Garza Bermea.
 */
class PastDueService : Service() {

    override fun onCreate() {
        Log.d(TAG, "PastDue Service has been created")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "PastDue Service has been started")
        checkDueDates()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null // we don't provide binding

    private fun checkDueDates() {
        fetchCarData()?.let { car ->
            processPastRepairDatesList(
                filterPastDueServices(car.services)
            )
        } ?: stopService().also {
            Log.i(TAG, "No past due dates found for today")
        }
    }

    private fun fetchCarData(): Car? = prefs.defaultCar

    private fun filterPastDueServices(serviceList: List<CarService>): List<CarService> =
        serviceList.filter { it.isPastDue() }

    private fun processPastRepairDatesList(pastDueServiceList: List<CarService>) {
        // TODO: Schedule Notification based on the Setting's constraints
        pastDueServiceList.forEach {
            Log.v(TAG, "Service ${it.name} is past due with date")
        }
        stopService()
    }

    private fun stopService() {
        Log.v(TAG, "PastDue Service is being stopped")
        // TODO: Schedule another alarm
        stopSelf()
    }
}