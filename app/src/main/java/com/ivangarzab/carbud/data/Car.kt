package com.ivangarzab.carbud.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

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
}