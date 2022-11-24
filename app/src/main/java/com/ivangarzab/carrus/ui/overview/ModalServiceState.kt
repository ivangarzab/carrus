package com.ivangarzab.carrus.ui.overview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class ModalServiceState(
    val name: String,
    val repairDate: String,
    val dueDate: String,
    val brand: String,
    val type: String,
    val price: String
) : Parcelable {

    fun isEmpty(): Boolean = name.isEmpty() && repairDate.isEmpty() && dueDate.isEmpty()
}
