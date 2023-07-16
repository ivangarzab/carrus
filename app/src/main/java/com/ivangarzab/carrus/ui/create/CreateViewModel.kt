package com.ivangarzab.carrus.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.util.extensions.setState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class CreateViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val carRepository: CarRepository
    ) : ViewModel() {

    val state: LiveData<CarModalState> = savedState.getLiveData(
        STATE,
        CarModalState()
    )
    enum class Type { CREATE, EDIT }
    lateinit var type: Type

    val onSubmit: MutableLiveData<Boolean> = MutableLiveData(false)

    fun verifyData(
        make: String,
        model: String,
        year: String
    ): Boolean = make.isNotBlank() && model.isNotBlank() && year.isNotBlank()

    fun onSubmitData() {
        state.value?.let { state ->
            Timber.v("Saving car data")
            Car(
                uid = UUID.randomUUID().toString(),
                nickname = state.nickname,
                make = state.make,
                model = state.model,
                year = state.year,
                licenseNo = state.licenseNo,
                vinNo = state.vinNo,
                tirePressure = state.tirePressure,
                totalMiles = state.totalMiles,
                milesPerGallon = state.milesPerGallon,
                services = emptyList(),
                imageUri = state.imageUri
            ).let { data ->
                when (type) {
                    Type.CREATE -> carRepository.saveCarData(data)
                    Type.EDIT -> carRepository.saveCarData(data.copy(
                        services = carRepository.fetchCarData()?.services ?: emptyList()
                    ))
                }
            }
        }
        onSubmit.postValue(true)
    }

    fun onImageUriReceived(uri: String) {
        setState(state, savedState, STATE) {
            copy(imageUri = uri)
        }
    }

    fun onImageDeleted() {
        setState(state, savedState, STATE) {
            copy(imageUri = null)
        }
    }

    fun onUpdateStateData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseNo: String,
        vinNo: String,
        tirePressure: String,
        totalMiles: String,
        milesPerGallon: String
    ) {
        setState(state, savedState, STATE) {
            copy(
                nickname = nickname,
                make = make,
                model = model,
                year = year,
                licenseNo = licenseNo,
                vinNo = vinNo,
                tirePressure = tirePressure,
                totalMiles = totalMiles,
                milesPerGallon = milesPerGallon
            )
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
                carRepository.saveCarData(adaptCarData(
                    car.copy(imageUri = null) // get rid of the image URL to avoid exception
                ))
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