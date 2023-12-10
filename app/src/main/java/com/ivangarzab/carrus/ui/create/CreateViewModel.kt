package com.ivangarzab.carrus.ui.create

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.structures.LiveState
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.managers.Analytics
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
    private val carRepository: CarRepository,
    private val contentResolverHelper: ContentResolverHelper,
    private val analytics: Analytics
) : ViewModel() {

    val state: LiveState<CarModalState> = LiveState(CarModalState())

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    enum class Type { CREATE, EDIT }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
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
                licenseState = state.licenseState,
                licenseNo = state.licenseNo,
                vinNo = state.vinNo,
                tirePressure = state.tirePressure,
                totalMiles = state.totalMiles,
                milesPerGalCity = state.milesPerGalCity,
                milesPerGalHighway = state.milesPerGalHighway,
                services = emptyList(),
                imageUri = state.imageUri
            ).let { data ->
                when (type) {
                    Type.CREATE -> {
                        carRepository.saveCarData(data)
                        analytics.logCarCreated(data.uid, data.getCarName())
                    }
                    Type.EDIT -> {
                        carRepository.saveCarData(data.copy(
                            services = carRepository.fetchCarData()?.services ?: emptyList()
                        ))
                        analytics.logCarUpdated(data.uid, data.getCarName())
                    }
                }
            }
        }
        onSubmit.postValue(true)
    }

    fun onImageUriReceived(uri: String) {
        if (uri.isBlank()) return

        Timber.v("Image uri '$uri' permission ${
            when (contentResolverHelper.persistUriPermission(uri)) {
                true -> "granted"
                false -> "denied"
            }
        }")
        analytics.logImageAdded()
        state.setState {
            copy(imageUri = uri)
        }
    }

    fun onImageDeleted() {
        Timber.v("Deleting image")
        analytics.logImageDeleted()
        state.setState {
            copy(imageUri = null)
        }
    }

    fun onUpdateStateData(
        nickname: String,
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
        state.setState {
            copy(
                nickname = nickname,
                make = make,
                model = model,
                year = year,
                licenseState = licenseState,
                licenseNo = licenseNo,
                vinNo = vinNo,
                tirePressure = tirePressure,
                totalMiles = totalMiles,
                milesPerGalCity = milesPerGalCity,
                milesPerGalHighway = milesPerGalHighway
            )
        }
    }

    private fun onSetupContent(car: Car) {
        state.setState {
            copy(
                title = "Edit Car",
                actionButton = "Update",
                nickname = car.nickname,
                make = car.make,
                model = car.model,
                year = car.year,
                licenseState = car.licenseState,
                licenseNo = car.licenseNo,
                vinNo = car.vinNo,
                tirePressure = car.tirePressure,
                totalMiles = car.totalMiles,
                milesPerGalCity = car.milesPerGalCity,
                milesPerGalHighway = car.milesPerGalHighway,
                imageUri = car.imageUri
            )
        }
    }

    fun onImportData(data: String): Boolean {
        return try {
            CarImporter.importFromJson(data)?.let { car ->
                Timber.d("Got car data to import: $car")
                carRepository.saveCarData(car)
                analytics.logCarImported(car.uid, car.getCarName())
                onSubmit.postValue(true)
                true
            } ?: false
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            false
        }
    }
}