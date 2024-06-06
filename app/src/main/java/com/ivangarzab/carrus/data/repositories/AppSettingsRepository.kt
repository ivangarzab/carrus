package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.Flow

/**
 * The purpose of this class is to serve as the main interface contract for the app settings repository.
 */
interface AppSettingsRepository {

    val appSettingsFlow: Flow<AppSettingsState>
    suspend fun setTimeFormatSetting(format: TimeFormat)
    suspend fun setLeftHandedSetting(isLeftHanded: Boolean)
    suspend fun setDueDateFormatSetting(format: DueDateFormat)
}
