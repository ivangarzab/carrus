package com.ivangarzab.carrus.data.models

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

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
    val licenseState: String,
    val licenseNo: String,
    val vinNo: String,
    val tirePressure: String,
    val totalMiles: String,
    val milesPerGalCity: String,
    val milesPerGalHighway: String,
    var services: List<Service>,
    val imageUri: String? = null
) : Parcelable, Comparable<Car> {

    fun toJson(): String = Gson().toJson(this)

    //TODO: Propagate into the OverviewScreen
    fun getCarName(): String = nickname.ifBlank { "$make $model" }

    override fun compareTo(other: Car): Int = compareValuesBy(this, other,
        { it.uid },
        { it.nickname },
        { it.make },
        { it.model },
        { it.year },
        { it.licenseState },
        { it.licenseNo },
        { it.vinNo },
        { it.tirePressure },
        { it.totalMiles },
        { it.milesPerGalCity },
        { it.milesPerGalHighway },
    )

    override fun toString(): String {
        return "Car ${getCarName()} with ${services.size} services"
    }

    fun toStringVerbose(): String {
        return "Car(" +
                "\nnickname='$nickname'" +
                "\nmake='$make'" +
                "\nmodel='$model'" +
                "\nyear='$year'" +
                "\nlicenseState='$licenseState'" +
                "\nlicenseNo='$licenseNo'" +
                "\nvinNo='$vinNo'" +
                "\ntirePressure='$tirePressure'" +
                "\ntotalMiles='$totalMiles'" +
                "\nmi/gal-City='$milesPerGalCity'" +
                "\nmi/gal-Highway='$milesPerGalHighway'" +
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
            licenseState = "",
            licenseNo = "",
            vinNo = "",
            tirePressure = "",
            totalMiles = "",
            milesPerGalCity = "",
            milesPerGalHighway = "",
            services = emptyList()
        )
        val default: Car = Car(
            uid = "123",
            nickname = "Shaq",
            make = "Chevrolet",
            model = "Malibu",
            year = "2006",
            licenseState = "Texas",
            licenseNo = "IGB066",
            vinNo = "4Y1SL65848Z411439",
            tirePressure = "35",
            totalMiles = "99,999",
            milesPerGalCity = "26",
            milesPerGalHighway = "31",
            services = Service.serviceList
        )
    }
}