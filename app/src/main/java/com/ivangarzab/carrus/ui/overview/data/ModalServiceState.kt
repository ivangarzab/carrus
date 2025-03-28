package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Deprecated("Moved into Compose")
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

    companion object {
        val empty = ModalServiceState(
            name = "",
            repairDate = "",
            dueDate = "",
            brand = "",
            type = "",
            price = "",
        )
    }
}
