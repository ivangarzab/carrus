package com.ivangarzab.carbud.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.serviceList
import com.ivangarzab.carbud.repositories.CarRepository
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModel : ViewModel() {

    private val carRepository = CarRepository()

    val onSubmit: MutableLiveData<Boolean> = MutableLiveData(false)

    fun submitData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseNo: String = "xxxxxx"
    ) {
        Log.v("IGB", "Saving default car")
        carRepository.saveCar(Car(
            uid = UUID.randomUUID().toString(),
            nickname = nickname,
            make = make,
            model = model,
            year = year,
            licenseNo = licenseNo,
            tirePressure = "",
            totalMiles = "",
            milesPerGallon = "",
            services = serviceList, // TODO: hardcoded for testing
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