package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Deprecated("Moved into Compose")
@Parcelize
data class ItemServiceState(
    var position: Int,
    var expanded: Boolean = false,
    var name: String,
    var dueDateFormat: String,
    var repairDateFormat: String,
    var details: String,
    var price: String
) : Parcelable
