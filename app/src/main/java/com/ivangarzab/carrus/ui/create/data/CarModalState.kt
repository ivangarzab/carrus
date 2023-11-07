package com.ivangarzab.carrus.ui.create.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class CarModalState(
    val isExpanded: Boolean = false,
    val title: String = "Add a Car",
    val actionButton: String = "Submit",
    val nickname: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val licenseState: String = "",
    val licenseNo: String = "",
    val vinNo: String = "",
    val tirePressure: String = "",
    val totalMiles: String = "",
    val milesPerGalCity: String = "",
    val milesPerGalHighway: String = "",
    val imageUri: String? = null
) : Parcelable