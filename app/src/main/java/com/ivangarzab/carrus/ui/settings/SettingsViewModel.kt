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
        updateDueDateFormatState(appSettingsRepository.fetchDueDateFormatSetting())
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
        viewModelScope.launch {
            alarmSettingsRepository.observeAlarmSettingsData().collect {
                updateAlarmTimeState(it.alarmTime)
            }
        }
    }

    fun isNight(): Boolean = appSettingsRepository.fetchNightThemeSetting() ?: false

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

    fun onAlarmTimePicked(alarmTime: String) {
        Timber.d("'Past Due' alarm time reset to: ${getTimeString(alarmTime.toInt())}")
        alarmSettingsRepository.setAlarmTime(alarmTime.toInt())
        //TODO: Revisit and reconsider this next call
        alarmsRepository.schedulePastDueAlarm(true)
        updateAlarmTimeState(alarmTime)
    }

    fun onDueDateFormatPicked(option: String) {
        DueDateFormat.get(option).let { dueDateFormat ->
            Timber.d("Due Date format changed to: '$dueDateFormat'")
            appSettingsRepository.setDueDateFormatSetting(dueDateFormat)
            updateDueDateFormatState(dueDateFormat)
        }

    }

    fun getDueDateFormat(): DueDateFormat = appSettingsRepository.fetchDueDateFormatSetting()

    fun getTimeString(hour: Int): String = "$hour:00 ${
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
                Timber.w("Unable to import car data",)
                return false
            } ?: Timber.w("Unable to parse data from file with uri: $uri")
        }
        Timber.w("Unable to import data from uri path")
        return false
    }

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    private fun updateAlarmTimeState(alarmTime: String) {
        setState(state, savedState, STATE) { copy(alarmTime = alarmTime) }
    }

    private fun updateDueDateFormatState(format: DueDateFormat) {
        setState(state, savedState, STATE) { copy(dueDateFormat = format) }
    }

    fun getAlarmTime() = alarmSettingsRepository.getAlarmTime()

    val pickerOptionsAlarmTime = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
    )
    val pickerOptionsDueDateFormat = arrayOf(
        "days", "weeks", "months", "due date"
    )

    companion object {
        private const val STATE: String = "SettingsViewModel.STATE"
    }
}