package com.ivangarzab.carrus.ui.overview

import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.data.di.BuildVersionProvider
import com.ivangarzab.carrus.data.di.DebugFlagProvider
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.Message
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.MessageQueueRepository
import com.ivangarzab.carrus.data.structures.LiveState
import com.ivangarzab.carrus.data.structures.asUniqueMessageQueue
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewStaticState
import com.ivangarzab.carrus.ui.overview.data.ServiceItemState
import com.ivangarzab.carrus.ui.overview.data.ServicePanelState
import com.ivangarzab.carrus.ui.overview.data.SortingType
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.isPastDue
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val buildVersionProvider: BuildVersionProvider,
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val messageQueueRepository: MessageQueueRepository,
    private val analytics: Analytics,
    private val debugFlagProvider: DebugFlagProvider
) : ViewModel() {

    val staticState: LiveState<OverviewStaticState> = LiveState(OverviewStaticState())
    val detailsPanelState: LiveState<DetailsPanelState> = LiveState(DetailsPanelState())
    val servicePanelState: LiveState<ServicePanelState> = LiveState(ServicePanelState())
    val serviceModalState: LiveState<ServiceModalState> = LiveState(ServiceModalState())

    val queueState: LiveState<MessageQueueState> = LiveState(MessageQueueState())

    private var hasPromptedForPermissionNotification: Boolean = false
    val triggerNotificationPermissionRequest: LiveEvent<Boolean> = LiveEvent()
    private var hasPromptedForPermissionAlarm: Boolean = false
    val triggerAlarmsPermissionRequest: LiveEvent<Boolean> = LiveEvent()

    var carDataInternal: Car? = null

    private var dueDateFormat: DueDateFormat = DueDateFormat.DAYS
    private var serviceSortingType: SortingType = SortingType.NONE

    init {
        viewModelScope.launch {
            carRepository.observeCarData()
                .catch { Timber.w("Something went wrong collecting the car data") }
                .collect {
                    Timber.d("Got a car update from the repository: $it")
                    processCarDataChange(it)
                }
        }
        viewModelScope.launch {
            appSettingsRepository.observeAppSettingsStateData().collect {
                if (dueDateFormat != it.dueDateFormat) {
                    dueDateFormat = it.dueDateFormat
                    updateServiceListWithNewDueDateFormat()
                }
            }
        }
        viewModelScope.launch {
            messageQueueRepository.observeMessageQueueFlow().collect {
                queueState.setState {
                    //TODO: Fix the cast
                    copy(messageQueue = it.asUniqueMessageQueue())
                }
            }
        }
    }

    private fun processCarDataChange(car: Car?) {
        // Save car data for future complex operations
        carDataInternal = car
        car?.let {
            // Update static state
            staticState.setState {
                copy(
                    carName = it.getCarName(),
                    isDataEmpty = false,
                    imageUri = it.imageUri
                )
            }
            // Update dynamic state(s)
            detailsPanelState.setState { DetailsPanelState.fromCar(it) }
            servicePanelState.setState {
                copy(serviceItemList = generateServiceItemStateList(it.services))
            }
        } ?: resetScreenState()
    }

    private fun generateServiceItemStateList(
        services: List<Service>
    ): List<ServiceItemState> = services.mapIndexed { index, service ->
        ServiceItemState(
            index = index,
            name = service.name,
            details = "${service.brand} - ${service.type}",
            price = NumberFormat.getCurrencyInstance().format(service.cost),
            repairDate = "on ${service.repairDate.getShortenedDate()}",
            dueDateFormatted = when (service.isPastDue()) {
                true -> "DUE"
                false -> (service.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                    TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                        when (daysLeft) {
                            0L -> "Tomorrow"
                            else -> when (dueDateFormat) {
                                DueDateFormat.DATE -> service.dueDate.getShortenedDate()
                                DueDateFormat.WEEKS -> "${
                                    String.format(
                                        "%.1f",
                                        daysLeft / MULTIPLIER_DAYS_TO_WEEKS
                                    )
                                } weeks"
                                DueDateFormat.MONTHS -> "${String.format(
                                    "%.2f",
                                    daysLeft / MULTIPLIER_DAYS_TO_MONTHS
                                )} mo."
                                else -> "$daysLeft days"
                            }
                        }
                    }
                }
            },
            isPastDue = service.isPastDue(),
            data = service
        )
    }

    //TODO: Break this function down into more digestible and manageable pieces
    fun processServiceDataChange(
        isServiceListEmpty: Boolean,
        areNotificationsEnabled: Boolean
    ) {
        Timber.v("Processing overview state change")
        if (isServiceListEmpty) {
            when (areNotificationsEnabled) {
                true -> {
                    if (alarmsRepository.isPastDueAlarmActive().not()) {
                        alarmsRepository.schedulePastDueAlarm()
                    } else {
                        Timber.v("'Past Due' alarm is already scheduled")
                    }
                }
                false -> {
                    if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.TIRAMISU &&
                        hasPromptedForPermissionNotification.not()
                    ) {
                        addNotificationPermissionMessage()
                    } else {
                        Timber.v("We don't need Notification permission for sdk=${buildVersionProvider.getSdkVersionInt()} (<33)")
                    }
                }
            }
        }
    }

    private fun resetScreenState() {
        //TODO: Reset everything!
        staticState.setState {
            copy(
                carName = "",
                isDataEmpty = true
            )
        }
    }

    fun onServiceCreate() {
        Timber.d("Creating a new service")
        serviceModalState.setState {
            ServiceModalState(
                ServiceModalState.Mode.CREATE
            )
        }
    }

    fun onServiceEdit(service: Service) {
        Timber.d("Editing service: $service")
        serviceModalState.setState {
            ServiceModalState.fromService(service, ServiceModalState.Mode.EDIT)
        }
    }

    fun onServiceReschedule(service: Service) {
        Timber.d("Rescheduling service: $service")
        serviceModalState.setState {
            ServiceModalState.fromService(service, ServiceModalState.Mode.RESCHEDULE)
        }
    }

    fun onServiceCompleted(service: Service) {
        Timber.d("Service completed: $service")
        analytics.logServiceCompleted(service.id, service.name)
        //TODO: Update services completed count property
        onServiceDeleted(service)
    }

    fun onServiceDeleted(service: Service) {
        Timber.d("Service being deleted: $service")
        carRepository.removeCarService(service)
        analytics.logServiceDeleted(service.id, service.name)
    }

    fun onServiceModalDismissed() {
//        Timber.v("Service modal dismissed")
        serviceModalState.setState { ServiceModalState() }
    }

    fun onNotificationPermissionActivityResult(isGranted: Boolean) {
        Timber.d("Notification permissions ${if (isGranted) "granted" else "denied"}")
        if (isGranted) {
            removeNotificationPermissionMessage()
        }
        analytics.logNotificationPermissionResult(isGranted)
    }

    fun onMessageClicked(id: String) {
        Timber.d("Got a message click with id=$id")
        analytics.logAppMessageClicked(id)
        when (id) {
            Message.MISSING_PERMISSION_NOTIFICATION.data.id -> {
                if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.TIRAMISU) {
                    triggerNotificationPermissionRequest.postValue(true)
                }
                removeNotificationPermissionMessage()
            }

            Message.MISSING_PERMISSION_ALARM.data.id -> {
                if (buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.S) {
                    triggerAlarmsPermissionRequest.postValue(true)
                }
                removeAlarmPermissionMessage()
            }

            Message.TEST.data.id -> removeMessage(Message.TEST)
        }
    }

    @VisibleForTesting
    fun addNotificationPermissionMessage() = with(Message.MISSING_PERMISSION_NOTIFICATION) {
        addMessage(this)
        hasPromptedForPermissionNotification = true
    }

    private fun removeNotificationPermissionMessage() =
        with(Message.MISSING_PERMISSION_NOTIFICATION) {
            removeMessage(this)
        }

    fun checkForAlarmPermission(canScheduleExactAlarms: Boolean) {
        if (canScheduleExactAlarms.not() &&
            hasPromptedForPermissionAlarm.not() &&
            buildVersionProvider.getSdkVersionInt() >= Build.VERSION_CODES.S
        ) {
            addAlarmPermissionMessage()
            Timber.d("Registering alarm permission state changed broadcast receiver")
        }
    }

    @VisibleForTesting
    fun addAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        addMessage(this)
        hasPromptedForPermissionAlarm = true
    }

    private fun removeAlarmPermissionMessage() = with(Message.MISSING_PERMISSION_ALARM) {
        removeMessage(this)
    }

    fun addTestMessage() {
        if (debugFlagProvider.isDebugEnabled()) {
            addMessage(Message.TEST)
        }
    }

    private fun addMessage(type: Message) = with(type) {
        Timber.v("Adding ${this.name} message to the queue")
        analytics.logAppMessageAdded(this.name)
        messageQueueRepository.addMessage(this)
    }

    private fun removeMessage(type: Message) = with(type) {
        Timber.v("Removing ${this.name} message from queue")
        analytics.logAppMessageRemoved(this.name)
        messageQueueRepository.removeMessage(this)
    }

    fun onMessageDismissed() {
        Timber.v("Removing message at the top of the queue")
        //TODO: How do we instrument this, or should we simply use removeMessage instead?
        messageQueueRepository.dismissMessage()
    }

    private fun onSortingByType(type: SortingType) {
        when (type) {
            SortingType.NONE -> resetServicesSort()
            SortingType.NAME -> sortServicesByName()
            SortingType.DUE_DATE -> sortServicesByDueDate()
            SortingType.REPAIR_DATE -> sortServicesByRepairDate()
        }
        analytics.logServiceListSorted(type.name)
        serviceSortingType = type
    }

    private fun resetServicesSort() {
        carDataInternal?.let { car ->
            Timber.v("Resetting services sorting")
            updateSortedList(car.services)
        }
    }

    private fun sortServicesByName() {
        carDataInternal?.let { car ->
            Timber.v("Sorting services by name")
            updateSortedList(car.services.sortedBy { it.name })
        }
    }

    private fun sortServicesByDueDate() {
        carDataInternal?.let { car ->
            Timber.v("Sorting services by due date")
            updateSortedList(car.services.sortedBy { it.dueDate })
        }
    }

    private fun sortServicesByRepairDate() {
        carDataInternal?.let { car ->
            Timber.v("Sorting services by repair date")
            updateSortedList(car.services.sortedBy { it.repairDate })
        }
    }

    private fun updateSortedList(sortedServiceList: List<Service>) {
        servicePanelState.setState {
            copy(serviceItemList = generateServiceItemStateList(sortedServiceList))
        }
    }

    fun onSort(type: SortingType) {
        Timber.v("Got a sorting request with type=$type")
        analytics.logSortClicked(type.name)
        servicePanelState.setState {
            copy(
                selectedSortingOption = when (type) {
                    SortingType.NONE -> 0
                    SortingType.NAME -> 1
                    SortingType.REPAIR_DATE -> 2
                    SortingType.DUE_DATE -> 3
                }
            )
        }
        onSortingByType(type)
    }

    private fun updateServiceListWithNewDueDateFormat() {
        carDataInternal?.let {
            servicePanelState.setState {
                copy(serviceItemList = generateServiceItemStateList(it.services))
            }
        }
    }

    fun setupEasterEggForTesting() {
        if (debugFlagProvider.isDebugEnabled()) {
            carRepository.saveCarData(Car.default)
        }
    }

    override fun onCleared() {
        Timber.v("Clearing OverviewViewModel")
        super.onCleared()
    }
}

private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f