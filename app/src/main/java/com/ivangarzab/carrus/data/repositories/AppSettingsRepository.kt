package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ivan Garza Bermea.
 */
interface AppSettingsRepository {

    val appSettingsStateFlow: StateFlow<AppSettingsState>
    val leftHandedModeFlow: StateFlow<Boolean>

    fun getVersionNumber(): String
    fun setNightThemeSetting(isNight: Boolean)
    fun observeAppSettingsStateData(): Flow<AppSettingsState>
    fun setDueDateFormatSetting(format: DueDateFormat)
    fun setTimeFormatSetting(format: TimeFormat)
    fun observeLeftHandedData(): Flow<Boolean>
}