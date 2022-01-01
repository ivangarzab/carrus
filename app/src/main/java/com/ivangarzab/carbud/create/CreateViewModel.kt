package com.ivangarzab.carbud.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.repositories.CarRepository
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModel : ViewModel() {

    private val carRepository = CarRepository()

    fun submitData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseNo: String = "xxxxxx"
    ) {
        Log.v("IGB", "Saving default car")
        carRepository.saveCar(Car(
            UUID.randomUUID().toString(),
            true,
            nickname,
            make,
            model,
            year,
            licenseNo,
            0
        ))
    }

    fun verifyData(
        make: String,
        model: String,
        year: String
    ): Boolean = make.isNotBlank() && model.isNotBlank() && year.isNotBlank()
}