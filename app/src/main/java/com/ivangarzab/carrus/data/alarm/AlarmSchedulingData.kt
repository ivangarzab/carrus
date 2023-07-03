package com.ivangarzab.carrus.data.alarm

/**
 * The purpose of this enumeration class is to serve as a data holder for either scheduling or
 * canceling request made to the [com.ivangarzab.carrus.util.AlarmScheduler] directly.
 *
 * Created by Ivan Garza Bermea.
 */
data class AlarmSchedulingData(
    val type: Alarm,
    val frequency: AlarmFrequency,
    val triggerTime: Long, // Calendar.timeInMillis
    val intentRequestCode: Int,
    val intentAction: String,
)
