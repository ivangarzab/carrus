package com.ivangarzab.carrus.ui.create

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.util.extensions.setState
import com.ivangarzab.carrus.util.managers.CarImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class CreateViewModel @Inject constructor(
    private val carRepository: CarRepository
    ) : ViewModel() {

    private val _state: MutableLiveData<CarModalState> = MutableLiveData(CarModalState())
    val state: LiveData<CarModalState> = _state
    enum class Type { CREATE, EDIT }
    lateinit var type: Type

    val onSubmit: MutableLiveData<Boolean> = MutableLiveData(false)

    val onVerify: LiveEvent<Boolean> = LiveEvent()

    fun init(data: Car?) {
        type = data?.let {
            onSetupContent(it)
            Type.EDIT
        } ?: Type.CREATE
    }

    fun verifyData(
        make: String,
        model: String,
        year: String
    ) {
        (make.isNotBlank() && model.isNotBlank() && year.isNotBlank()).let { result ->
            onVerify.value = result
            if (result) onSubmitData()
        }
    }

    private fun onSubmitData() {
        state.value?.let { state ->
            Timber.v("Saving car data")
            Car(
                uid = UUID.randomUUID().toString(),
                nickname = state.nickname,
                make = state.make,
                model = state.model,
                year = state.year,
                licenseState = state.licenseNo,
                licenseNo = state.licenseNo,
                vinNo = state.vinNo,
                tirePressure = state.tirePressure,
                totalMiles = state.totalMiles,
                milesPerGalCity = state.milesPerGalCity,
                milesPerGalHighway = state.milesPerGalCity,
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

    fun onImageUriReceived(contentResolver: ContentResolver, uri: String) {
        contentResolver.takePersistableUriPermission(
            Uri.parse(uri),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        setState(state, _state) {
            copy(imageUri = uri)
        }
    }

    fun onImageDeleted() {
        Timber.v("Deleting image")
        setState(state, _state) {
            copy(imageUri = null)
        }
    }

    fun onUpdateStateData(
        nickname: String = "",
        make: String,
        model: String,
        year: String,
        licenseState: String,
        licenseNo: String,
        vinNo: String,
        tirePressure: String,
        totalMiles: String,
        milesPerGalCity: String,
        milesPerGalHighway: String
    ) {
        setState(state, _state) {
            copy(
                nickname = nickname,
                make = make,
                model = model,
                year = year,
                licenseNo = licenseNo,
                vinNo = vinNo,
                tirePressure = tirePressure,
                totalMiles = totalMiles,
                milesPerGalCity = milesPerGalCity
            )
        }
    }

    private fun onSetupContent(car: Car) {
        setState(state, _state) {
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
                milesPerGalCity = car.milesPerGalCity,
                imageUri = car.imageUri
            )
        }
    }

    fun onImportData(data: String): Boolean {
        return try {
            CarImporter.importFromJson(data)?.let { car ->
                Timber.d("Got car data to import: $car")
                carRepository.saveCarData(car)
                onSubmit.postValue(true)
                true
            } ?: false
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            false
        }
    }
}