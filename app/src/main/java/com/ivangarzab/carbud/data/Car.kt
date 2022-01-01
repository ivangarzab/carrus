package com.ivangarzab.carbud.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
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
    @DrawableRes val profileImage: Int
) : Parcelable {

    fun convertToJson(): String = Gson().toJson(this)

    override fun toString(): String {
        return "Car(" +
                "\nnickname='$nickname'" +
                "\nmake='$make'" +
                "\nmodel='$model'" +
                "\nyear='$year'" +
                "\nlicenseNo='$licenseNo'" +
                "\nprofileImage=$profileImage)"
    }


    companion object {
        val emptyCar: Car = Car(
            uid = UUID.randomUUID().toString(),
            default = false,
            nickname = "",
            make = "",
            model = "",
            year = "",
            licenseNo = "",
            profileImage = 0
        )
    }
}