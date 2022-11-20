package com.ivangarzab.carrus.ui.settings

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ivangarzab.carrus.alarms
import com.ivangarzab.carrus.carRepository
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.extensions.setState
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    @Parcelize
    data class SettingsState(
        val car: Car? = null,
        val alarmTime: String? = null,
        val dueDateFormat: DueDateFormat = DueDateFormat.DAYS
    ) : Parcelable

    val state: LiveData<SettingsState> = savedState.getLiveData(
        STATE,
        SettingsState()
    )

    init {
        updateAlarmTimeState(prefs.alarmPastDueTime?.toString() ?: "$DEFAULT_ALARM_TIME")
        updateDueDateFormatState(prefs.dueDateFormat)
        viewModelScope.launch {
            carRepository.observeCarData().collect {
                updateCarState(it)
            }
        }
    }

    fun onDarkModeToggleClicked(checked: Boolean) {
        Timber.v("Dark mode toggle was checked to: $checked")
        prefs.darkMode = checked
        setDefaultNightMode(checked)
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
                alarms.cancelPastDueAlarm() // Make sure to cancel any scheduled alarms
            }
        } ?: Timber.v("There are no services to delete from car data")
    }

    private fun setDefaultNightMode(isNight: Boolean) {
        when (isNight) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onAlarmTimePicked(alarmTime: String) {
        Timber.d("'Past Due' alarm time reset to: ${getTimeString(alarmTime.toInt())}")
        prefs.alarmPastDueTime = alarmTime.toInt()
        alarms.schedulePastDueAlarm(true)
        updateAlarmTimeState(alarmTime)
    }

    fun onDueDateFormatPicked(option: DueDateFormat) {
        Timber.d("Due Date format changed to: '$option'")
        prefs.dueDateFormat = option
        updateDueDateFormatState(option)

    }

    fun getTimeString(hour: Int): String = "$hour:00 ${
        when (hour) {
            in 1..12 -> "AM"
            in 13..24 -> "PM"
            else -> ""
        }
    }"

    fun getExportData(): String? = carRepository.fetchCarData()?.let { data ->
        Gson().toJson(data)
    }

    fun onImportData(data: String): Boolean {
        return try {
            Gson().fromJson(data, Car::class.java).let { car ->
                Timber.d("Got car data to import: $car")
                carRepository.saveCarData(car)
            }
            true
        } catch (e: Exception) {
            Timber.w("Unable to import data", e)
            false
        }
    }

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    private fun updateAlarmTimeState(alarmTime: String) {
        setState(state, savedState, STATE) { copy(alarmTime = alarmTime) }
    }

    private fun updateDueDateFormatState(format: DueDateFormat) {
        setState(state, savedState, STATE) { copy(dueDateFormat = format) }
    }

    val pickerOptionsAlarmTime = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
    )
    val pickerOptionsDueDateFormat = arrayOf(
        "days", "weeks", "months", "due date"
    )

    companion object {
        const val DEFAULT_ALARM_TIME: Int = 7
        private const val STATE: String = "SettingsViewModel.STATE"
    }
}