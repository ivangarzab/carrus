package com.ivangarzab.carrus.ui.overview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
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
