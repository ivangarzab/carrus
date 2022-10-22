package com.ivangarzab.carbud.ui.settings

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivangarzab.carbud.alarms
import com.ivangarzab.carbud.carRepository
import com.ivangarzab.carbud.data.Car
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.extensions.setState
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    @Parcelize
    data class SettingsState(
        val car: Car? = null
    ) : Parcelable

    val state: LiveData<SettingsState> = savedState.getLiveData(
        STATE,
        SettingsState()
    )

    init {
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

    private fun updateCarState(car: Car?) =
        setState(state, savedState, STATE) { copy(car = car) }

    companion object {
        private const val STATE: String = "SettingsViewModel.STATE"
    }
}