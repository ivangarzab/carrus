package com.ivangarzab.carrus.data

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
                name = "Oil Change",
                repairDate = Calendar.getInstance().apply { timeInMillis = 1639120980000 },
                dueDate = Calendar.getInstance().apply { timeInMillis = 1672590100000 },
                brand = "Armor All",
                type = "Synthetic",
                cost = 79.99f
            ),
            Service(
                id = "2",
                name = "Window Wipes",
                repairDate = Calendar.getInstance().apply { timeInMillis = 1662358975427 },
                dueDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
                brand = "Walmart",
                type = "6'', long",
                cost = 25.00f
            ),
            Service(
                id = "3",
                name = "Tires",
                repairDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
                dueDate = Calendar.getInstance().apply { timeInMillis = 1662916020000 },
                brand = "Michelin",
                type = "24''",
                cost = 500.69f
            ),
            Service(
                id = "4",
                name = "Rims",
                repairDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
                dueDate = Calendar.getInstance().apply { timeInMillis = 1697276100000 },
                brand = "Auto Zone Express",
                type = "24'', black",
                cost = 420.00f
            )
        )
    }
}