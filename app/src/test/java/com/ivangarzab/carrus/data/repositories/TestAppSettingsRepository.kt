package com.ivangarzab.carrus.data.repositories

import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ivan Garza Bermea.
 */
class TestAppSettingsRepository : AppSettingsRepository {

    @VisibleForTesting
    var _appSettingsStateFlow = MutableStateFlow(AppSettingsState())
    override val appSettingsStateFlow: StateFlow<AppSettingsState>
        get() = _appSettingsStateFlow

    private var _leftHandedModeFlow = MutableStateFlow(false)
    override val leftHandedModeFlow: StateFlow<Boolean>
        get() = _leftHandedModeFlow

    var isNight: Boolean? = null

    var isLefty: Boolean = DEFAULT_LEFT_HANDED_MODE_SETTING

    override fun getVersionNumber(): String {
        return DEFAULT_VERSION_NUMBER
    }

    override fun setNightThemeSetting(isNight: Boolean) {
        this.isNight = isNight
    }

    override fun observeAppSettingsStateData(): Flow<AppSettingsState> {
        return appSettingsStateFlow
    }

    override fun setDueDateFormatSetting(format: DueDateFormat) {
        appSettingsStateFlow.value.let {
            _appSettingsStateFlow.value = it.copy(
                dueDateFormat = format
            )
        }
    }

    override fun setTimeFormatSetting(format: TimeFormat) {
        appSettingsStateFlow.value.let {
            _appSettingsStateFlow.value = it.copy(
                timeFormat = format
            )
        }
    }

    override fun observeLeftHandedData(): Flow<Boolean> {
        return leftHandedModeFlow
    }

    override fun setLeftHandedSetting(isLeftHanded: Boolean) {
        isLefty = isLeftHanded
    }

    companion object {
        const val DEFAULT_VERSION_NUMBER: String = "0.0.0"
        const val DEFAULT_DARK_THEME_SETTING: Boolean = false
        const val DEFAULT_LEFT_HANDED_MODE_SETTING: Boolean = false
    }
}