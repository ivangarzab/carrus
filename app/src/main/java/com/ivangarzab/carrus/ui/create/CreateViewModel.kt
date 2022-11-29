package com.ivangarzab.carrus.ui.create

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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
        val title: String = "Add a Car",
        val actionButton: String = "Submit",
        val nickname: String = "",
        val make: String = "",
        val model: String = "",
        val year: String = "",
        val licenseNo: String = "",
        val vinNo: String = "",
        val tirePressure: String = "",
        val totalMiles: String = "",
        val milesPerGallon: String = "",
        val imageUri: String? = null
    ) : Parcelable

    val state: LiveData<CarModalState> = savedState.getLiveData(
        STATE,
        CarModalState()
    )
    enum class Type { CREATE, EDIT }
    lateinit var type: Type

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
        Car(
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
        ).let { data ->
            when (type) {
                Type.CREATE -> carRepository.saveCarData(data)
                Type.EDIT -> carRepository.saveCarData(data.copy(
                    services = carRepository.fetchCarData()?.services ?: emptyList()
                ))
            }
        }
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

    fun onSetupContent(car: Car) {
        setState(state, savedState, STATE) {
            copy(
                title = "Edit Car",
                actionButton = "Update",
                nickname = car.nickname,
                make = car.make,
                model = car.model,
                year = car.year,
                licenseNo = car.licenseNo,
                vinNo = car.vinNo,
                tirePressure = car.tirePressure,
                totalMiles = car.totalMiles,
                milesPerGallon = car.milesPerGallon,
                imageUri = car.imageUri
            )
        }
    }

    fun onImportData(data: String): Boolean {
        return try {
            Gson().fromJson(data, Car::class.java).let { car ->
                Timber.d("Got car data to import: $car")
                carRepository.saveCarData(adaptCarData(car))
            }
            onSubmit.postValue(true)
            true
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            false
        }
    }

    /**
     * TODO: There's got to be a better way of doing this!
     *  Or at least arrive at at state where we don't need this anymore --
     *  Should we start versioning our Car data too?
     */
    private fun adaptCarData(data: Car): Car = Car (
        uid = UUID.randomUUID().toString(),
        nickname = data.nickname ?: "",
        make = data.make ?: "",
        model = data.model ?: "",
        year = data.year ?: "",
        licenseNo = data.licenseNo ?: "",
        vinNo = data.vinNo ?: "",
        tirePressure = data.tirePressure ?: "",
        totalMiles = data.totalMiles ?: "",
        milesPerGallon = data.milesPerGallon ?: "",
        services = data.services,
        imageUri = null
    )

    companion object {
        private const val STATE: String = "CreateViewModel.STATE"
    }
}