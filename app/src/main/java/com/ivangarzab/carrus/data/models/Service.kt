package com.ivangarzab.carrus.data.models

import android.os.Parcelable
import com.ivangarzab.carrus.util.extensions.getFormattedDate
import kotlinx.parcelize.Parcelize
import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
const val VERSION_SERVICE: Int = 1
@Parcelize
data class Service(
    val version: Int = VERSION_SERVICE,
    val id: String,
    val name: String,
    val repairDate: Calendar,
    val dueDate: Calendar,
    val brand: String? = null,
    val type: String? = null,
    val cost: Float = 0.00f
): Parcelable, Comparable<Service> {

    override fun compareTo(other: Service): Int = compareValuesBy(this, other,
        { it.id },
        { it.name },
        { it.repairDate.timeInMillis },
        { it.dueDate.timeInMillis },
        { it.brand },
        { it.type },
        { it.cost },
    )

    override fun toString(): String =
        "Service(" +
                "\nname='$name'" +
                "\nrepairDate='${repairDate.getFormattedDate()}'" +
                "\ndueDate='${dueDate.getFormattedDate()}'" +
                "\nbrand='$brand'" +
                "\ntype='$type'" +
                "\ncost='$cost'" +
                "\n)"

    companion object {
        val empty: Service = Service(
            id = "-1",
            name = "Empty Service",
            repairDate = Calendar.getInstance(),
            dueDate = Calendar.getInstance()
        )

        val serviceList: List<Service> = listOf(
            Service(
                id = "1",
                name = "alpha",
                repairDate = Calendar.getInstance(),
                dueDate = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 7)
                },
                brand = "the brand",
                type = "type & spec",
                cost = 99.99f
            ),
            Service(
                id = "2",
                name = "zetta",
                repairDate = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                },
                dueDate = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    add(Calendar.DAY_OF_MONTH, 1)
                },
            ),
            Service(
                id = "3",
                name = "omega",
                repairDate = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                },
                dueDate = Calendar.getInstance().apply {
                    add(Calendar.MONTH, 1)
                },
                brand = "brand",
                type = "type/spec",
                cost = 150.00f
            )
        )
    }
}