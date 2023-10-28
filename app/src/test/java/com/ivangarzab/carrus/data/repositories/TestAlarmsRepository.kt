package com.ivangarzab.carrus.data.repositories

/**
 * Created by Ivan Garza Bermea.
 */
class TestAlarmsRepository : AlarmsRepository {

    var isPastDueAlarmScheduled: Boolean = false

    override fun cancelAllAlarms() {
        isPastDueAlarmScheduled = false
    }

    override fun isPastDueAlarmActive(): Boolean {
        return isPastDueAlarmScheduled
    }

    override fun schedulePastDueAlarm(force: Boolean) {
        isPastDueAlarmScheduled = true
    }

}