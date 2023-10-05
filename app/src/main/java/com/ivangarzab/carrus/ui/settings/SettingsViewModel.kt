package com.ivangarzab.carrus.ui.settings

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.AlarmSettingsState
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.TimeFormat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import com.ivangarzab.carrus.util.extensions.readFromFile
import com.ivangarzab.carrus.util.extensions.writeInFile
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.carrus.util.managers.CarExporter
import com.ivangarzab.carrus.util.managers.CarImporter
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val alarmSettingsRepository: AlarmSettingsRepository
    ) : ViewModel() {

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
                updateDueDateFormatState(it.dueDateFormat)
                updateTimeFormatState(it.timeFormat)
            }

        }
        viewModelScope.launch {
            alarmSettingsRepository.observeAlarmSettingsData().collect {
                processAlarmSettingsStateUpdate(it)
            }
        }
    }

    fun onDarkModeToggleClicked(checked: Boolean) {
        Timber.v("Dark mode toggle was checked to: $checked")
        appSettingsRepository.setNightThemeSetting(checked)
        Analytics.logNightThemeChange(checked)
    }

    fun onDeleteCarDataClicked() {
        Timber.d("Deleting car data")
        carData?.let {
            Analytics.logCarDelete(it.uid, it.getCarName())
        }
        carRepository.deleteCarData()
    }

    fun onDeleteServicesClicked() {
        carData?.let {
            if (it.services.isNotEmpty()) {
                Timber.d("Deleting all services from car data")
                val newCar = it.copy(
                    services = emptyList()
                )
                carRepository.saveCarData(newCar)
                alarmsRepository.cancelAllAlarms()
                Analytics.logServiceListDeleted(it.uid)
            }
        } ?: Timber.wtf("There are no services to delete from car data")
    }

    fun onAlarmsToggled(enabled: Boolean) {
        Timber.d("Alarms enabled toggled: $enabled")
        alarmSettingsRepository.toggleAlarmFeature(enabled)
        Analytics.logAlarmFeatureToggle(enabled)
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
        state.value?.let { state ->
            val newAlarmTime = AlarmTime(alarmTime)
            Timber.d("Alarm time selected: ${newAlarmTime.getTimeAsString(state.clockTimeFormat)}")
            alarmSettingsRepository.setAlarmTime(alarmTime)
            Analytics.logAlarmTimeChange(alarmTime)
            if (state.alarmTime.equals(newAlarmTime).not()) {
                Timber.d("Rescheduling alarm after the time was changed")
                rescheduleAlarms()
            }
        }
    }

    fun onAlarmFrequencyPicked(frequency: AlarmFrequency) {//TODO: Pass in a String instead to keep logic inside the VM
        Timber.d("Alarm frequency selected: ${frequency.value}")
        alarmSettingsRepository.setAlarmFrequency(frequency)
        Analytics.logAlarmFrequencySet(frequency.value)
        Timber.d("Rescheduling alarm after the frequency was changed")
        rescheduleAlarms()
    }

    fun onDueDateFormatPicked(option: String) {
        DueDateFormat.get(option).let { dueDateFormat ->
            Timber.d("Due Date format changed to: '$dueDateFormat'")
            appSettingsRepository.setDueDateFormatSetting(dueDateFormat)
            Analytics.logDueDateFormatChange(option)
        }

    }

    fun onClockTimeFormatPicked(option: String) {
        TimeFormat.get(option).let { timeFormat ->
            Timber.d("Clock Time format changed to: '$timeFormat'")
            appSettingsRepository.setTimeFormatSetting(timeFormat)
            Analytics.logTimeFormatChange(option)
        }
    }

    private fun rescheduleAlarms() {
        //TODO: Revisit and reconsider this next call
        alarmsRepository.schedulePastDueAlarm(true)
    }

    fun onExportData(
        contentResolver: ContentResolver,
        uri: Uri
    ): Boolean = carRepository.fetchCarData()?.let { data ->
        Analytics.logCarExported(data.uid, data.getCarName())
        CarExporter.exportToJson(data)?.let { json ->
            appScope.launch(Dispatchers.IO) {
                uri.writeInFile(contentResolver, json)
            }
            true
        } ?: false
    } ?: false

    fun onImportData(
        contentResolver: ContentResolver,
        uri: Uri
    ): Boolean {
        uri.readFromFile(contentResolver).let { data ->
            data?.let {
                CarImporter.importFromJson(data)?.let { car ->
                    carRepository.saveCarData(car)
                    Analytics.logCarImported(car.uid, car.getCarName())
                    return true
                }
                Timber.w("Unable to import car data")
                return false
            } ?: Timber.w("Unable to parse data from file with uri: $uri")
        }
        Timber.w("Unable to import data from uri path")
        return false
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
        updateAlarmsEnabledState(data.isAlarmPermissionGranted && data.isAlarmFeatureEnabled)
        updateAlarmTimeState(data.alarmTime)
        updateAlarmFrequencyState(data.frequency)
    }

    private fun updateAlarmsEnabledState(enabled: Boolean) {
        Timber.v("Updating alarms toggle enabled state to $enabled")
        _state.value = state.value?.copy(alarmsOn = enabled)
    }

    private fun updateAlarmTimeState(alarmTime: AlarmTime) {
        Timber.v("Updating alarm time state to $alarmTime")
        state.value?.let { state ->
            _state.value = state.copy(alarmTime = alarmTime)
        }
    }

    private fun updateAlarmFrequencyState(frequency: AlarmFrequency) {
        Timber.v("Updating alarm frequency state to $frequency")
        _state.value = state.value?.copy(alarmFrequency = frequency)
    }

    private fun updateDueDateFormatState(format: DueDateFormat) {
        Timber.v("Updating due date format state to $format")
        _state.value = state.value?.copy(dueDateFormat = format)
    }

    private fun updateTimeFormatState(format: TimeFormat) {
        Timber.v("Updating clock time format state to $format")
        state.value?.let { currentState ->
            _state.value = currentState.copy(
                clockTimeFormat = format,
                alarmTimeSubtitle = when (format) {
                    TimeFormat.HR24 -> R.string.setting_alarm_time_subtitle_24
                    TimeFormat.HR12 -> R.string.setting_alarm_time_subtitle_12
                },
                alarmTimeOptions = format.range.map { it.toString() }
            )
        }
    }
}