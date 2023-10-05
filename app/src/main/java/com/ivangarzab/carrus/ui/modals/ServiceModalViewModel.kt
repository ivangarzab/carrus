package com.ivangarzab.carrus.ui.modals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.util.extensions.empty
import com.ivangarzab.carrus.util.extensions.getCalendarFromShortenedDate
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.parseIntoMoney
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class ServiceModalViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _state = MutableLiveData(ServiceModalState())
    val state: LiveData<ServiceModalState> = _state

    private val _onSubmission = LiveEvent<Boolean>()
    val onSubmission: LiveData<Boolean> = _onSubmission

    enum class Type { CREATE, EDIT }
    lateinit var modalType: Type //TODO: Clear this out with inheritance

    private var validatedService: Service? = null

    fun setArgsData(data: Service?) {
        modalType = data?.let {
            validatedService = it
            setState(ServiceModalState(
                title = "Update Service",
                name = data.name,
                repairDate = data.repairDate.getShortenedDate(),
                dueDate = data.dueDate.getShortenedDate(),
                brand = data.brand,
                type = data.type,
                price = "%.2f".format(data.cost)
            ))
            Type.EDIT
        } ?: Type.CREATE.also { setState(ServiceModalState(title = "Create Service")) }
    }

    fun onUpdateServiceModalState(update: ServiceModalState) = setState(update)

    fun onActionButtonClicked() {
        Timber.v("Action button clicked")
        attemptToValidateService()
        _onSubmission.value = validatedService?.let {
            when (verifyServiceData(it)) {
                true -> {
                    Timber.v("Submitting Service data")
                    when (modalType) {
                        Type.CREATE -> onServiceCreated(it)
                        Type.EDIT -> onServiceUpdate(it)
                    }
                    true
                }
                false -> false
            }
        } ?: false
    }

    private fun verifyServiceData(data: Service): Boolean =
        data.name.isNotBlank() &&
                data.repairDate.timeInMillis != 0L &&
                data.dueDate.timeInMillis != 0L

    private fun verifyServiceData(
        name: String?,
        repairDate: String?,
        dueDate: String?
    ): Boolean =
        if (name != null && repairDate != null && dueDate != null) {
            name.isNotBlank() &&
                    verifyStringDate(repairDate) &&
                    verifyStringDate(dueDate)
        } else false

    private fun verifyStringDate(stringDate: String): Boolean {
        stringDate.takeIf {
            it.isNotBlank()
        }?.getCalendarFromShortenedDate().let { date ->
            if (date?.timeInMillis != 0L) {
                return true
            }
        }
        return false
    }

    private fun onServiceCreated(service: Service) {
        carRepository.addCarService(service)
        Timber.d("New service created: ${service.name}")
        Analytics.logServiceCreate(service.id, service.name)
    }

    private fun onServiceUpdate(service: Service) {
        carRepository.updateCarService(service)
        Timber.d("Service updated: ${service.name}")
    }

    private fun setState(update: ServiceModalState) {
        Timber.v("Setting state: $update")
        _state.value = update
        attemptToValidateService()
    }

    private fun attemptToValidateService() {
        Timber.v("Attempting to validate Service data from state")
        state.value?.let {
            if (verifyServiceData(it.name, it.repairDate, it.dueDate)) {
                Timber.v("Validation successful")
                setValidatedService(it)
            } else Timber.v("Validation failed")
        }
    }

    private fun setValidatedService(data: ServiceModalState) {
        if (validatedService == null) {
            validatedService = Service(
                id = UUID.randomUUID().toString(),
                name = data.name ?: "",
                repairDate = data.repairDate?.getCalendarFromShortenedDate() ?: Calendar
                    .getInstance()
                    .empty(),
                dueDate = data.dueDate?.getCalendarFromShortenedDate() ?: Calendar
                    .getInstance()
                    .empty(),
                brand = data.brand,
                type = data.type,
                cost = data.price?.parseIntoMoney() ?: DEFAULT_COST
            )
        } else {
            validatedService?.let { current ->
                validatedService = current.copy(
                    id = current.id,
                    name = data.name ?: current.name,
                    repairDate = data.repairDate?.getCalendarFromShortenedDate() ?: current.repairDate,
                    dueDate = data.dueDate?.getCalendarFromShortenedDate() ?: current.dueDate,
                    brand = data.brand ?: current.brand,
                    type = data.type ?: current.type,
                    cost = data.price?.parseIntoMoney() ?: DEFAULT_COST
                )
            }
        }
    }

    companion object {
        private const val DEFAULT_COST = 0.00f
        const val DEFAULT_DUE_DATE_ADDITION: Int = 7
    }
}