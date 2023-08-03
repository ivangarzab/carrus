package com.ivangarzab.carrus.ui.settings

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ivangarzab.carrus.appScope
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.TimeFormat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import com.ivangarzab.carrus.util.extensions.readFromFile
import com.ivangarzab.carrus.util.extensions.setState
import com.ivangarzab.carrus.util.extensions.writeInFile
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
    private val savedState: SavedStateHandle,
    private val carRepository: CarRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val alarmsRepository: AlarmsRepository,
    private val alarmSettingsRepository: AlarmSettingsRepository
    ) : ViewModel() {

    val state: LiveData<SettingsState> = savedState.getLiveData(
        STATE,
        SettingsState()
    )

    init {
        //TODO: This is a mess!
        updateDueDateFormatState(appSettingsRepository.fetchDueDateFormatSetting())
        updateTimeFormatState(appSettingsRepository.fetchTimeFormatSetting())
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
        viewModelScope.launch {
            alarmSettingsRepository.observeAlarmSettingsData().collect {
                // TODO: Get rid of this redundancy
                updateAlarmsEnabledState(it.areAlarmsEnabled)
                updateAlarmTimeState(it.alarmTime)
                updateAlarmFrequencyState(it.frequency)
            }
        }
    }

    fun onDarkModeToggleClicked(checked: Boolean) {
        Timber.v("Dark mode toggle was checked to: $checked")
        appSettingsRepository.setNightThemeSetting(checked)
    }

    fun onDeleteCarDataClicked() {
        Timber.d("Deleting car data")
        carRepository.deleteCarData()
    }

    fun onDeleteServicesClicked() {
        state.value?.car?.let {
            if (it.services.isNotEmpty()) {
                Timber.d("Deleting all services from car data")
                val newCar = it.copy(
                    services = emptyList()
                )
                carRepository.saveCarData(newCar)
                alarmsRepository.cancelAllAlarms()
            }
        } ?: Timber.v("There are no services to delete from car data")
    }

    fun onAlarmsEnabledToggleClicked(enabled: Boolean) {
        Timber.d("Alarms enabled toggled: $enabled")
        //TODO: Check alarm permission first!
        alarmSettingsRepository.setAreAlarmsEnabled(enabled)
        updateAlarmsEnabledState(enabled)
    }

    fun onAlarmTimePicked(alarmTime: String) {
        Timber.d("'Past Due' alarm time reset to: ${getTimeString(alarmTime.toInt())}")
        alarmSettingsRepository.setAlarmTime(alarmTime.toInt())
        //TODO: Revisit and reconsider this next call
        alarmsRepository.schedulePastDueAlarm(true)
        updateAlarmTimeState(alarmTime)
    }

    fun onAlarmFrequencyPicked(frequency: AlarmFrequency) {//TODO: Pass in a String instead to keep logic inside the VM
        Timber.d("Alarms frequency selected: ${frequency.value}")
        alarmSettingsRepository.setAlarmFrequency(frequency)
        updateAlarmFrequencyState(frequency)
    }

    fun onDueDateFormatPicked(option: String) {
        DueDateFormat.get(option).let { dueDateFormat ->
            Timber.d("Due Date format changed to: '$dueDateFormat'")
            appSettingsRepository.setDueDateFormatSetting(dueDateFormat)
            updateDueDateFormatState(dueDateFormat)
        }

    }

    fun onClockTimeFormatPicked(option: String) {
        TimeFormat.get(option).let { timeFormat ->
            Timber.d("Clock Time format changed to: '$timeFormat'")
            appSettingsRepository.setTimeFormatSetting(timeFormat)
            updateTimeFormatState(timeFormat)
        }
    }

    private fun getTimeString(hour: Int): String = "$hour:00 ${
        when (hour) {
            in 1..12 -> "AM"
            in 13..24 -> "PM"
            else -> ""
        }
    }"

    fun onExportData(
        contentResolver: ContentResolver,
        uri: Uri
    ): Boolean = carRepository.fetchCarData()?.let { data ->
        Gson().toJson(data)?.let { json ->
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
                    return true
                }
                Timber.w("Unable to import car data")
                return false
            } ?: Timber.w("Unable to parse data from file with uri: $uri")
        }
        Timber.w("Unable to import data from uri path")
        return false
    }

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    private fun updateAlarmsEnabledState(enabled: Boolean) {
        setState(state, savedState, STATE) { copy(alarmsOn = enabled) }
    }

    private fun updateAlarmTimeState(alarmTime: String) {
        setState(state, savedState, STATE) { copy(alarmTime = alarmTime) }
    }

    private fun updateAlarmFrequencyState(frequency: AlarmFrequency) {
        setState(state, savedState, STATE) { copy(alarmFrequency = frequency) }
    }

    private fun updateDueDateFormatState(format: DueDateFormat) {
        setState(state, savedState, STATE) { copy(dueDateFormat = format) }
    }

    private fun updateTimeFormatState(format: TimeFormat) {
        setState(state, savedState, STATE) {
            copy(
                clockTimeFormat = format,
                alarmTimeOptions = format.range.map { it.toString() }
            )
        }
    }

    companion object {
        private const val STATE: String = "SettingsViewModel.STATE"
    }
}