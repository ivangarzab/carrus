package com.ivangarzab.carrus.util.alarms

import com.ivangarzab.carrus.data.AlarmFrequency

/**
 * The purpose of this enumeration class is to serve as a data holder for either scheduling or
 * canceling request made to the [AlarmScheudler] directly.
 *
 * Created by Ivan Garza Bermea.
 */
data class AlarmSchedulingData(
    val type: AlarmType,
    val frequency: AlarmFrequency,
    val triggerTime: Long, // Calendar.timeInMillis
    val intentRequestCode: Int,
    val intentAction: String,
) {

}
