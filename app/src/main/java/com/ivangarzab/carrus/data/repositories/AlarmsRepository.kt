package com.ivangarzab.carrus.data.repositories

/**
 * The purpose of this file is to hold and manage information about particular alarms, as well as
 * scheduling new alarms whenever it is required.
 *
 * Created by Ivan Garza Bermea.
 */
interface AlarmsRepository {
    fun cancelAllAlarms()
    fun isPastDueAlarmActive(): Boolean
    fun schedulePastDueAlarm(force: Boolean = false)
}