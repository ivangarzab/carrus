package com.ivangarzab.carbud.ui.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.TAG
import com.ivangarzab.carbud.carRepository
import com.ivangarzab.carbud.data.Car
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModel : ViewModel() {

    val onSubmit: MutableLiveData<Boolean> = MutableLiveData(false)

    fun submitData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseNo: String = "xxxxxx"
    ) {
        Log.v(TAG, "Saving default car")
        carRepository.saveCarData(Car(
            uid = UUID.randomUUID().toString(),
            nickname = nickname,
            make = make,
            model = model,
            year = year,
            licenseNo = licenseNo,
            tirePressure = "",
            totalMiles = "",
            milesPerGallon = "",
            services = emptyList(),
            profileImage = 0
        ))
        onSubmit.postValue(true)
    }

    fun verifyData(
        make: String,
        model: String,
        year: String
    ): Boolean = make.isNotBlank() && model.isNotBlank() && year.isNotBlank()
}