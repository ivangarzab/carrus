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
    val default: Boolean,
    val nickname: String,
    val make: String,
    val model: String,
    val year: String,
    val licenseNo: String,
    var parts: List<Part>,
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
                "\nparts='$parts'" +
                "\nprofileImage=$profileImage)"
    }

    companion object {
        val empty: Car = Car(
            uid = "",
            default = false,
            nickname = "",
            make = "",
            model = "",
            year = "",
            licenseNo = "",
            parts = emptyList(),
            profileImage = 0
        )
        val default: Car = Car(
            uid = "123",
            default = false,
            nickname = "Shaq",
            make = "Chevrolet",
            model = "Malibu",
            year = "2006",
            licenseNo = "IGB066",
            parts = listOf(
                Part(
                    name = "Oil Change",
                    lastDate = Calendar.getInstance().apply { timeInMillis = 1639037220000 },
                    dueDate = Calendar.getInstance().apply { timeInMillis = 1667286420000 }
                ),
                Part(
                    name = "Window Wipes",
                    lastDate = Calendar.getInstance().apply { timeInMillis = 1651734420000 },
                    dueDate = Calendar.getInstance().apply { timeInMillis = 1669882020000 }
                ),
                Part(
                    name = "Tires",
                    lastDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
                    dueDate = Calendar.getInstance().apply { timeInMillis = 1662016020000 }
                ),
                Part(
                    name = "Rims",
                    lastDate = Calendar.getInstance().apply { timeInMillis = 1644909780000 },
                    dueDate = Calendar.getInstance().apply { timeInMillis = 1662016020000 }
                )
            ),
            profileImage = 0
        )
    }
}

@Parcelize
data class Part(
    val name: String,
    val lastDate: Calendar,
    val dueDate: Calendar
): Parcelable {
    override fun toString(): String {
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        return "Part(" +
                "\nname='$name'" +
                "\nlastDate='${format.format(lastDate.time)}'" +
                "\ndueDate='${format.format(dueDate.time)}'"
    }
}