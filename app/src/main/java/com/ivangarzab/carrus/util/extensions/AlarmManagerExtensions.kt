package com.ivangarzab.carrus.util.extensions

import android.app.AlarmManager
import android.os.Build

/**
 * Created by Ivan Garza Bermea.
 */
fun AlarmManager.isAbleToScheduleExactAlarms(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    canScheduleExactAlarms()
} else {
    true
}