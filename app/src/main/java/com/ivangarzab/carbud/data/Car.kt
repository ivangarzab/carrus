package com.ivangarzab.carbud.data

import android.os.Parcelable
import com.google.gson.Gson
import com.ivangarzab.carbud.util.extensions.getFormattedDate
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class Car(
    val uid: String,
    val nickname: String,
    val make: String,
    val model: String,
    val year: String,
    val licenseNo: String,
    val tirePressure: String,
    val totalMiles: String,
    val milesPerGallon: String,
    var services: List<Service>,
    val imageUri: String? = null
) : Parcelable {

    fun toJson(): String = Gson().toJson(this)

    override fun toString(): String {
        return "Car(" +
                "\nnickname='$nickname'" +
                "\nmake='$make'" +
                "\nmodel='$model'" +
                "\nyear='$year'" +
                "\nlicenseNo='$licenseNo'" +
                "\nservices='$services'" +
                "\nimageUri=$imageUri" +
                "\n)"
    }

    companion object {
        val empty: Car = Car(
            uid = "",
            nickname = "",
            make = "",
            model = "",
            year = "",
            licenseNo = "",
            tirePressure = "",
            totalMiles = "",
            milesPerGallon = "",
            services = emptyList()
        )
        val default: Car = Car(
            uid = "123",
            nickname = "Shaq",
            make = "Chevrolet",
            model = "Malibu",
            year = "2006",
            licenseNo = "IGB066",
            tirePressure = "35",
            totalMiles = "99,999",
            milesPerGallon = "26",
            services = serviceList
        )
    }
}

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
): Parcelable {
    override fun toString(): String =
        "Service(" +
                "\nname='$name'" +
                "\nrepairDate='${repairDate.getFormattedDate()}'" +
                "\ndueDate='${dueDate.getFormattedDate()}'" +
                "\nbrand='$brand'" +
                "\ntype='$type'" +
                "\ncost='$cost'" +
                "\n)"
}

fun Service.isPastDue(): Boolean = this.dueDate.timeInMillis < Calendar.getInstance().timeInMillis

const val VERSION_SERVICE: Int = 1
val serviceList: List<Service> = listOf(
    Service(
        id = "1",
        name = "Oil Change",
        repairDate = Calendar.getInstance().apply { timeInMillis = 1639120980000 },
        dueDate = Calendar.getInstance().apply { timeInMillis = 1672550100000 },
        brand = "Armor All",
        type = "Synthetic",
        cost = 79.99f
    ),
    Service(
        id = "2",
        name = "Window Wipes",
        repairDate = Calendar.getInstance().apply { timeInMillis = 1662358975427 },
        dueDate = Calendar.getInstance().apply { timeInMillis = 1669882020000 },
        brand = "Walmart",
        type = "6'', long",
        cost = 25.00f
    ),
    Service(
        id = "3",
        name = "Tires",
        repairDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
        dueDate = Calendar.getInstance().apply { timeInMillis = 1662016020000 },
        brand = "Michelin",
        type = "24''",
        cost = 500.69f
    ),
    Service(
        id = "4",
        name = "Rims",
        repairDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
        dueDate = Calendar.getInstance().apply { timeInMillis = 1667276100000 },
        brand = "Auto Zone Express",
        type = "24'', black",
        cost = 420.00f
    )
)