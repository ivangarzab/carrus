package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.DueDateFormat
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class OverviewState(
    val car: Car? = null,
    val dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    val serviceSortingType: SortingType = SortingType.NONE,
    val hasPromptedForPermissionNotification: Boolean = false,
    val hasPromptedForPermissionAlarm: Boolean = false
) : Parcelable
