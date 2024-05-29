package com.ivangarzab.carrus.ui.modal_service

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.structures.LiveState
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.util.extensions.empty
import com.ivangarzab.carrus.util.extensions.getCalendarFromShortenedDate
import com.ivangarzab.carrus.util.extensions.parseIntoMoney
import com.ivangarzab.carrus.util.managers.Analytics
import timber.log.Timber
import java.util.Calendar
import java.util.UUID

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceModalViewModel(
    private val carRepository: CarRepository,
    private val analytics: Analytics
) : ViewModel() {

    val state: LiveState<ServiceModalState> = LiveState(ServiceModalState())

    val isShowingRepairDateDialog: LiveState<Boolean> = LiveState(false)
    val isShowingDueDateDialog: LiveState<Boolean> = LiveState(false)

    val modalMode: ServiceModalState.Mode
        get() = state.value?.mode ?: ServiceModalState.Mode.CREATE

    private val _onSubmission = LiveEvent<Boolean>()
    val onSubmission: LiveData<Boolean> = _onSubmission

    private var validatedService: Service? = null

    /**
     * Only call once. This call is necessary under the current design.
     */
    fun setInitialData(service: Service?, mode: ServiceModalState.Mode) {
        Timber.d("Setting initial data for a $mode modal")
        this.validatedService = service
        service?.let {
            ServiceModalState.fromService(it, mode).let { initialState ->
                setState(
                    if (initialState.mode == ServiceModalState.Mode.RESCHEDULE) {
                        // Show repair date dialog and nullify due date for reschedule flow.
                        isShowingRepairDateDialog.setState { true }
                        initialState.copy(dueDate = null)
                    } else {
                        // Use initialState as provided.
                        initialState
                    }
                )
            }
        } ?: setState(ServiceModalState(mode = mode))
    }

    fun onUpdateServiceModalState(update: ServiceModalState) = setState(update)

    fun onActionButtonClicked() {
        Timber.v("Action button clicked")
        attemptToValidateService()
        _onSubmission.value = validatedService?.let {
            when (verifyServiceData(it)) { // is this a redundant check?
                true -> {
                    Timber.v("Submitting Service data")
                    when (modalMode) {
                        ServiceModalState.Mode.CREATE -> onServiceCreated(it)
                        ServiceModalState.Mode.EDIT -> onServiceUpdate(it)
                        ServiceModalState.Mode.RESCHEDULE -> onServiceReschedule(it)
                        else -> { /* No-op */ }
                    }
                    // reset for next use
                    onClearState()
                    true
                }
                false -> false
            }
        } ?: false
    }

    fun onClearState() {
        Timber.v("Clearing out service data")
        setState(ServiceModalState())
        onCleared()
    }

    private fun verifyServiceData(data: Service): Boolean =
        data.name.isNotBlank() &&
                data.repairDate.timeInMillis != 0L &&
                data.dueDate.timeInMillis != 0L &&
                data.repairDate.timeInMillis < data.dueDate.timeInMillis

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

    @VisibleForTesting
    fun verifyStringDate(stringDate: String): Boolean {
        stringDate
            .takeIf { it.isNotBlank() }
            ?.getCalendarFromShortenedDate().let { date ->
                date?.let {
                    if (it.timeInMillis != 0L) {
                        return true
                    }
                }
            }
        return false
    }

    private fun onServiceCreated(service: Service) {
        carRepository.addCarService(service)
        Timber.d("New service created: ${service.name}")
        analytics.logServiceCreated(service.id, service.name)
    }

    private fun onServiceUpdate(service: Service) {
        carRepository.updateCarService(service)
        Timber.d("Service updated: ${service.name}")
        analytics.logServiceUpdated(service.id, service.name)
    }

    private fun onServiceReschedule(service: Service) {
        carRepository.updateCarService(service)
        Timber.d("Service rescheduled: ${service.name}")
        analytics.logServiceRescheduled(service.id, service.name)
    }

    private fun setState(update: ServiceModalState) {
        Timber.v("Setting state: $update")
        state.setState { update }
        attemptToValidateService()
    }

    private fun attemptToValidateService() {
        Timber.v("Attempting to validate Service data from state")
        state.value?.let {
            if (verifyServiceData(it.name, it.repairDate, it.dueDate)) {
                Timber.v("Validation successful")
                setValidatedService(it)
            } else Timber.v("Validation failed")
        } //TODO: Not handling the null state case
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

    fun onShowRepairDateDialog() {
        Timber.v("Showing repair date dialog")
        isShowingRepairDateDialog.setState { true }
    }

    fun onHideRepairDateDialog() {
        Timber.v("Hiding repair date dialog")
        isShowingRepairDateDialog.setState { false }
        // Trigger the next Calendar dialog, as needed
        if (state.value?.dueDate.isNullOrBlank()) {
            onShowDueDateDialog()
        }
    }

    fun onShowDueDateDialog() {
        Timber.v("Showing due date dialog")
        isShowingDueDateDialog.setState { true }
    }

    fun onHideDueDateDialog() {
        Timber.v("Hiding due date dialog")
        isShowingDueDateDialog.setState { false }
    }

    companion object {
        private const val DEFAULT_COST = 0.00f
        const val DEFAULT_DUE_DATE_ADDITION: Int = 7
    }
}