package com.ivangarzab.carrus.ui.settings

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.alarm.Alarm
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.di.DebugFlagProvider
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.states.AlarmSettingsState
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.carrus.util.managers.CarExporter
import com.ivangarzab.carrus.util.managers.CarImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val alarmSettingsRepository: AlarmSettingsRepository,
    private val analytics: Analytics,
    private val debugFlagProvider: DebugFlagProvider,
    private val contentResolverHelper: ContentResolverHelper,
    private val carExporter: CarExporter,
    private val carImporter: CarImporter
) : ViewModel() {

    @Inject
    lateinit var appScope: CoroutineScope

    private val _state: MutableLiveData<SettingsState> = MutableLiveData(SettingsState())
    val state: LiveData<SettingsState> = _state

    private var isAlarmPermissionGranted = false
    val onRequestAlarmPermission: LiveEvent<Any> = LiveEvent()

    private var carData: Car? = null

    init {
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
        viewModelScope.launch {
            appSettingsRepository.observeAppSettingsStateData().collect {
                processAppSettingsStateUpdates(it.dueDateFormat, it.timeFormat)
            }

        }
        viewModelScope.launch {
            alarmSettingsRepository.observeAlarmSettingsData().collect {
                processAlarmSettingsStateUpdate(it)
            }
        }
    }

    fun onDarkModeToggleClicked(checked: Boolean) {
        analytics.logDarkModeToggleClicked()
        Timber.v("Dark mode toggle was checked to: $checked")
        appSettingsRepository.setNightThemeSetting(checked)
        analytics.logNightThemeChanged(checked)
    }

    fun onDeleteCarDataClicked() {
        analytics.logDeleteCarDataClicked()
        Timber.d("Deleting car data")
        carData?.let {
            analytics.logCarDeleted(it.uid, it.getCarName())
        }
        carRepository.deleteCarData()
    }

    fun onDeleteServicesClicked() {
        analytics.logDeleteServiceListClicked()
        carData?.let {
            if (it.services.isNotEmpty()) {
                Timber.d("Deleting all services from car data")
                val newCar = it.copy(
                    services = emptyList()
                )
                carRepository.saveCarData(newCar)
                analytics.logServiceListDeleted(it.uid)
                alarmsRepository.cancelAllAlarms()
                analytics.logAlarmCancelled(Alarm.PAST_DUE.name)
            }
        } ?: Timber.wtf("There are no services to delete from car data")
    }

    fun onAlarmsToggled(enabled: Boolean) {
        analytics.logAlarmsToggleClicked()
        Timber.d("Alarms enabled toggled: $enabled")
        alarmSettingsRepository.toggleAlarmFeature(enabled)
        analytics.logAlarmFeatureToggled(enabled)
        if (enabled && isAlarmPermissionGranted.not()) {
            Timber.v("Attempting to request alarms permission")
            onRequestAlarmPermission.value = Any()
        }
    }

    /**
     * Process the alarm time picker Dialog selection.
     *
     * @param alarmTime alarm time in 24-hour clock format
     */
    fun onAlarmTimePicked(alarmTime: Int) {
        analytics.logAlarmTimeClicked()
        state.value?.let { state ->
            val newAlarmTime = AlarmTime(alarmTime)
            Timber.d("Alarm time selected: ${newAlarmTime.getTimeAsString(state.clockTimeFormat)}")
            alarmSettingsRepository.setAlarmTime(alarmTime)
            analytics.logAlarmTimeChanged(alarmTime)
            if (state.alarmTime.equals(newAlarmTime).not()) {
                Timber.d("Rescheduling alarm after the time was changed")
                rescheduleAlarms()
            }
        }
    }

    fun onAlarmFrequencyPicked(frequency: AlarmFrequency) {//TODO: Pass in a String instead to keep logic inside the VM
        analytics.logAlarmFrequencyClicked()
        Timber.d("Alarm frequency selected: ${frequency.value}")
        alarmSettingsRepository.setAlarmFrequency(frequency)
        analytics.logAlarmFrequencyChanged(frequency.value)
        Timber.d("Rescheduling alarm after the frequency was changed")
        rescheduleAlarms()
    }

    fun onDueDateFormatPicked(option: String) {
        analytics.logDueDateFormatClicked()
        DueDateFormat.get(option).let { dueDateFormat ->
            Timber.d("Due Date format changed to: '$dueDateFormat'")
            appSettingsRepository.setDueDateFormatSetting(dueDateFormat)
            analytics.logDueDateFormatChanged(option)
        }

    }

    fun onClockTimeFormatPicked(option: String) {
        analytics.logTimeFormatClicked()
        TimeFormat.get(option).let { timeFormat ->
            Timber.d("Clock Time format changed to: '$timeFormat'")
            appSettingsRepository.setTimeFormatSetting(timeFormat)
            analytics.logTimeFormatChanged(option)
        }
    }

    //TODO: Needs testing
    fun onExportData(uri: Uri): Boolean {
        return carRepository.fetchCarData()?.let { data -> //TODO: Grab the state data instead?
            carExporter.exportToJson(data)?.let { json ->
                appScope.launch(Dispatchers.IO) {
                    contentResolverHelper.writeInFile(uri, json)
                }
                analytics.logCarExported(data.uid, data.getCarName())
                true
            } ?: false
        } ?: false
    }

    //TODO: Needs testing
    fun onImportData(uri: Uri): Boolean {
        contentResolverHelper.readFromFile(uri).let { data ->
            data?.let {
                carImporter.importFromJson(data)?.let { car ->
                    carRepository.saveCarData(car)
                    analytics.logCarImported(car.uid, car.getCarName())
                    return true
                }
                Timber.w("Unable to import car data")
                return false
            } ?: Timber.w("Unable to parse data from file with uri: $uri")
        }
        Timber.w("Unable to import data from uri path")
        return false
    }

    fun onDebugModeToggle() = with(debugFlagProvider) {
        this.forceDebug = forceDebug.not()
        Timber.i("Forced debug mode toggled: $forceDebug")
    }

    private fun rescheduleAlarms() {
        //TODO: Revisit and reconsider this next call
        alarmsRepository.schedulePastDueAlarm(true)
        analytics.logAlarmScheduled(Alarm.PAST_DUE.name, true)
    }

    private fun updateCarState(car: Car?) {
        Timber.v("Updating car state")
        this.carData = car
        _state.value = state.value?.copy(
            isThereCarData = car != null,
            isThereCarServicesData = car?.services?.isNotEmpty() ?: false
        )
    }

    private fun processAlarmSettingsStateUpdate(data: AlarmSettingsState) {
        isAlarmPermissionGranted = data.isAlarmPermissionGranted
        Timber.v("Updating alarm settings state changes")
        state.value?.let { state ->
            _state.value = state.copy(
                alarmsOn = data.isAlarmPermissionGranted && data.isAlarmFeatureEnabled,
                alarmTime = data.alarmTime,
                alarmFrequency = data.frequency
            )
        }
    }

    private fun processAppSettingsStateUpdates(
        dueDateFormat: DueDateFormat,
        clockTimeFormat: TimeFormat
    ) {
        Timber.v("Updating app settings state changes")
        _state.value = state.value?.copy(
            dueDateFormat = dueDateFormat,
            clockTimeFormat = clockTimeFormat,
            alarmTimeSubtitle = when (clockTimeFormat) {
                TimeFormat.HR24 -> R.string.setting_alarm_time_subtitle_24
                TimeFormat.HR12 -> R.string.setting_alarm_time_subtitle_12
            },
            alarmTimeOptions = clockTimeFormat.range.map { it.toString() }
        )
    }
}