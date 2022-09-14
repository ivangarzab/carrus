package com.ivangarzab.carbud.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
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
    @DrawableRes val profileImage: Int
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
                "\nprofileImage=$profileImage" +
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
            services = emptyList(),
            profileImage = 0
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
            services = serviceList,
            profileImage = 0
        )
    }
}

@Parcelize
data class Service(
    val name: String,
    val lastDate: Calendar,
    val dueDate: Calendar
): Parcelable {
    override fun toString(): String {
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        return "Service(" +
                "\nname='$name'" +
                "\nlastDate='${format.format(lastDate.time)}'" +
                "\ndueDate='${format.format(dueDate.time)}'" +
                "\n)"
    }
}

val serviceList: List<Service> = listOf(
    Service(
        "Oil Change",
        Calendar.getInstance().apply { timeInMillis = 1639120980000 },
        Calendar.getInstance().apply { timeInMillis = 1662016020000 }),
    Service(
        "Window Wipes",
        Calendar.getInstance().apply { timeInMillis = 1662358975427 },
        Calendar.getInstance().apply { timeInMillis = 1669882020000 }),
    Service(
        "Tires",
        Calendar.getInstance().apply { timeInMillis = 1644909780000 },
        Calendar.getInstance().apply { timeInMillis = 1662016020000 }),
    Service(
        "Rims",
        Calendar.getInstance().apply { timeInMillis = 1644909780000 },
        Calendar.getInstance().apply { timeInMillis = 1662016020000 })
)