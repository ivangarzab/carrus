package com.ivangarzab.carrus.ui.create

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ivangarzab.carrus.carRepository
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.util.extensions.setState
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModel(
    private val savedState: SavedStateHandle
    ) : ViewModel() {

    @Parcelize
    data class CarModalState(
        val isExpanded: Boolean = false,
        val imageUri: String? = null
    ) : Parcelable

    val state: LiveData<CarModalState> = savedState.getLiveData(
        STATE,
        CarModalState()
    )

    val onSubmit: MutableLiveData<Boolean> = MutableLiveData(false)

    fun submitData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseNo: String = "xxxxxx",
        vinNo: String = "---",
        tirePressure: String = "",
        totalMiles: String = "---",
        milesPerGallon: String = "0",
        imageUri: String? = null
    ) {
        Timber.v("Saving default car")
        carRepository.saveCarData(Car(
            uid = UUID.randomUUID().toString(),
            nickname = nickname,
            make = make,
            model = model,
            year = year,
            licenseNo = licenseNo,
            vinNo = vinNo,
            tirePressure = tirePressure,
            totalMiles = totalMiles,
            milesPerGallon = milesPerGallon,
            services = emptyList(),
            imageUri = imageUri
        ))
        onSubmit.postValue(true)
    }

    fun verifyData(
        make: String,
        model: String,
        year: String
    ): Boolean = make.isNotBlank() && model.isNotBlank() && year.isNotBlank()

    fun onImageUriReceived(uri: String) {
        setState(state, savedState, STATE) {
            copy(imageUri = uri)
        }
    }

    fun onExpandToggle() {
        setState(state, savedState, STATE) {
            copy(isExpanded = isExpanded.not())
        }
    }

    companion object {
        private const val STATE: String = "CreateViewModel.STATE"
    }
}