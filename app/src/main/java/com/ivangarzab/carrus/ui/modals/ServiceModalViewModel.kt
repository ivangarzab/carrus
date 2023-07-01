package com.ivangarzab.carrus.ui.modals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.data.repositories.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class ServiceModalViewModel @Inject constructor(
    private val carRepository: CarRepository
    ) : ViewModel() {

    enum class Type { CREATE, EDIT }
    data class ServiceModalState(
        val type: Type,
        val data: Service?
    )
    private val _state = MutableLiveData<ServiceModalState>(null)
    val state: LiveData<ServiceModalState> = _state

    data class DataRequest(
        val type: Type = Type.EDIT,
        val id: String = ""
    )
    private val _onDataRequest = LiveEvent<Pair<Boolean, DataRequest>>()
    val onDataRequest: LiveData<Pair<Boolean, DataRequest>> = _onDataRequest

    private val _onSubmission = LiveEvent<Boolean>()
    val onSubmission: LiveData<Boolean> = _onSubmission

    fun setArgsData(data: Service?) {
        setState(ServiceModalState(
            type = data?.let { Type.EDIT } ?: Type.CREATE,
            data = data
        ))
    }

    fun onActionButtonClicked() {
        _onDataRequest.value = state.value?.let {
            Pair(true, DataRequest(
                it.type,
                it.data?.id ?: ""
            ))
        } ?: Pair(false, DataRequest())
    }

    fun onSubmitData(data: Service) {
        Timber.v("Got service data submission")
        _onSubmission.value = when (verifyServiceData(data)) {
            true -> {
                when (state.value?.type) {
                    Type.CREATE -> onServiceCreated(data)
                    Type.EDIT -> onServiceUpdate(data)
                    else -> Timber.wtf("There was an error saving data due to the modal's type")
                }
                true
            }
            false -> false
        }
    }

    fun getRepairDateInMillis(): Long = state.value?.data?.repairDate?.timeInMillis ?: 0L

    fun setNewRepairDateInMillis(dateInMillis: Long) = state.value?.let {
        setState(it.copy(
            data = it.data?.copy(
                repairDate = Calendar.getInstance().apply {
                    timeInMillis = dateInMillis
                }
            )
        ))
    }

    fun getDueDateInMillis(): Long = state.value?.data?.dueDate?.timeInMillis ?: 0L

    fun setNewDueDateInMillis(dateInMillis: Long) = state.value?.let {
        setState(it.copy(
            data = it.data?.copy(
                dueDate = Calendar.getInstance().apply {
                    timeInMillis = dateInMillis
                }
            )
        ))
    }

    private fun verifyServiceData(data: Service): Boolean =
        data.name.isNotBlank() &&
                data.repairDate.timeInMillis != 0L &&
                data.dueDate.timeInMillis != 0L

    private fun onServiceCreated(service: Service) {
        carRepository.addCarService(service)
        Timber.d("New service created: $service")
    }

    private fun onServiceUpdate(service: Service) {
        carRepository.updateCarService(service)
        Timber.d("Service being updated: $service")
    }

    private fun setState(update: ServiceModalState) {
        _state.value = update
    }
}