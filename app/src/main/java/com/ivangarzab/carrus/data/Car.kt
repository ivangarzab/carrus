package com.ivangarzab.carrus.data

import android.os.Parcelable
import com.google.gson.Gson
import com.ivangarzab.carrus.util.extensions.getFormattedDate
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
    val vinNo: String,
    val tirePressure: String,
    val totalMiles: String,
    val milesPerGallon: String,
    var services: List<Service>,
    val imageUri: String? = null
) : Parcelable, Comparable<Car> {

    fun toJson(): String = Gson().toJson(this)

    override fun compareTo(other: Car): Int = compareValuesBy(this, other,
        { it.uid },
        { it.nickname },
        { it.make },
        { it.model },
        { it.year },
        { it.licenseNo },
        { it.vinNo },
        { it.tirePressure },
        { it.totalMiles },
        { it.milesPerGallon },
    )

    fun isEqualTo(other: Car) = this.uid == other.uid &&
            this.nickname == other.nickname &&
            this.make == other.make &&
            this.model == other.model &&
            this.year == other.year &&
            this.licenseNo == other.licenseNo &&
            this.vinNo == other.vinNo &&
            this.tirePressure == other.tirePressure &&
            this.totalMiles == other.totalMiles &&
            this.milesPerGallon == other.milesPerGallon
    //TODO: Compare Service list as well

    override fun toString(): String {
        return "Car(" +
                "\nnickname='$nickname'" +
                "\nmake='$make'" +
                "\nmodel='$model'" +
                "\nyear='$year'" +
                "\nlicenseNo='$licenseNo'" +
                "\nvinNo='$vinNo'" +
                "\ntirePressure='$tirePressure'" +
                "\ntotalMiles='$totalMiles'" +
                "\nmi/gal='$milesPerGallon'" +
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
            vinNo = "",
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
            vinNo = "4Y1SL65848Z411439",
            tirePressure = "35",
            totalMiles = "99,999",
            milesPerGallon = "26",
            services = serviceList
        )
    }
}