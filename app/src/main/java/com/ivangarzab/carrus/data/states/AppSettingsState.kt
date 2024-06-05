package com.ivangarzab.carrus.data.states

import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat

/**
 * Created by Ivan Garza Bermea.
 */
data class AppSettingsState(
    val timeFormat: TimeFormat = TimeFormat.DEFAULT,
    val dueDateFormat: DueDateFormat = DueDateFormat.DEFAULT,
    val leftHandedMode: Boolean = false
)
