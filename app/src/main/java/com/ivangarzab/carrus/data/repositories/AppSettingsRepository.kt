package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ivan Garza Bermea.
 */
interface AppSettingsRepository {

    val appSettingsFlow: Flow<AppSettingsState>
    suspend fun setTimeFormatSetting(format: TimeFormat)
    suspend fun setLeftHandedSetting(isLeftHanded: Boolean)
    suspend fun setDueDateFormatSetting(format: DueDateFormat)
}

//    fun getVersionNumber(): String
