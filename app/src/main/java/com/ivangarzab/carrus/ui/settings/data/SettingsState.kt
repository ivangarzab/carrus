package com.ivangarzab.carrus.ui.settings.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class SettingsState(
    val car: Car? = null,
    val alarmTime: String? = null,
    val dueDateFormat: DueDateFormat = DueDateFormat.DAYS
) : Parcelable
