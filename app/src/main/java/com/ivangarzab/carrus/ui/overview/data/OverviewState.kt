package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.Service
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class OverviewState(
    val car: Car? = null,
    val dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    val serviceSortingType: SortingType = SortingType.NONE,
) : Parcelable

data class OverviewStaticState(
    val isDataEmpty: Boolean = true,
    val carName: String = "",
    val imageUri: String? = null
)

data class OverviewServicesState(
    val serviceList: List<ServiceItemState> = emptyList(),
    val showSortingOptions: Boolean = false,
    val selectedSortingOption: Int = 0
)

data class ServiceItemState(
    val index: Int,
    val name: String,
    val details: String,
    val price: String,
    val repairDate: String,
    val dueDateFormatted: String,
    val isPastDue: Boolean,
    val data: Service
)
