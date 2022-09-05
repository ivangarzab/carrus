package com.ivangarzab.carbud.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.data.Part
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
            default = true,
            nickname = nickname,
            make = make,
            model = model,
            year = year,
            licenseNo = licenseNo,
            parts = listOf(
                Part("Oil Change", Calendar.getInstance().apply { timeInMillis = 1639120980000 }, Calendar.getInstance().apply { timeInMillis = 1662016020000 }),
                Part("Window Wipes", Calendar.getInstance().apply { timeInMillis = 1662358975427 }, Calendar.getInstance().apply { timeInMillis = 1669882020000 }),
                Part("Tires", Calendar.getInstance().apply { timeInMillis = 1644909780000 }, Calendar.getInstance().apply { timeInMillis = 1662016020000 }),
                Part("Rims", Calendar.getInstance().apply { timeInMillis = 1644909780000 }, Calendar.getInstance().apply { timeInMillis = 1662016020000 })
            ),
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