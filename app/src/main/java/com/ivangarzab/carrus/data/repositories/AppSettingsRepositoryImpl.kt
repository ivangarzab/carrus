package com.ivangarzab.carrus.data.repositories

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.common.util.VisibleForTesting
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import com.ivangarzab.carrus.util.managers.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepositoryImpl(
    context: Context,
    private val prefs: Preferences
) : AppSettingsRepository {

    private val _appSettingsStateFlow = MutableStateFlow(AppSettingsState())
    override val appSettingsStateFlow: StateFlow<AppSettingsState>
        get() = _appSettingsStateFlow

    //TODO: Arguably, the leftHandedMode would deserve their own Repository class or join with state
    private val _leftHandedModeFlow = MutableStateFlow(false)
    override val leftHandedModeFlow: StateFlow<Boolean>
        get() = _leftHandedModeFlow

    init {
        setNightThemeSetting(fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context))
        setDueDateFormatSetting(fetchDueDateFormatSetting())
        setTimeFormatSetting(fetchTimeFormatSetting())
        setLeftHandedSetting(fetchLeftHandedSetting())
    }

    override fun getVersionNumber(): String = "v${BuildConfig.VERSION_NAME}"

    override fun observeAppSettingsStateData(): Flow<AppSettingsState> = appSettingsStateFlow

    /** Night Theme/Dark Mode **/
    @VisibleForTesting
    fun fetchNightThemeSetting(): Boolean? = prefs.darkMode

    override fun setNightThemeSetting(isNight: Boolean) {
        Timber.d("Setting night theme to: $isNight")
        prefs.darkMode = isNight
        setAppDefaultNightTheme(isNight)
    }

    private fun setAppDefaultNightTheme(isNight: Boolean) = when (isNight) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun getNightThemeSettingFromSystem(context: Context): Boolean =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }.also { result ->
            Timber.i("Night theme set to: $result")
        }

    /** Due Date Format functions **/
    @VisibleForTesting
    fun fetchDueDateFormatSetting(): DueDateFormat = prefs.dueDateFormat

    override fun setDueDateFormatSetting(format: DueDateFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        prefs.dueDateFormat = format
        updateDueDateFormatFlow(format)
    }

    private fun updateDueDateFormatFlow(format: DueDateFormat) {
        appSettingsStateFlow.value.let {
            _appSettingsStateFlow.value = it.copy(
                dueDateFormat = format
            )
        }
    }

    /** Time Format functions **/
    @VisibleForTesting
    fun fetchTimeFormatSetting(): TimeFormat = prefs.timeFormat.also {
        Timber.v("Got time format: ${it.name}")
    }

    override fun setTimeFormatSetting(format: TimeFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        prefs.timeFormat = format
        updateTimeFormatFlow(format)
    }

    private fun updateTimeFormatFlow(format: TimeFormat) {
        appSettingsStateFlow.value.let {
            _appSettingsStateFlow.value = it.copy(
                timeFormat = format
            )
        }
    }

    /** Left-handed Mode functions **/
    @VisibleForTesting
    fun fetchLeftHandedSetting(): Boolean = prefs.leftHandedMode

    override fun setLeftHandedSetting(isLeftHanded: Boolean) {
        Timber.v("Setting left-handed mode: $isLeftHanded")
        prefs.leftHandedMode = isLeftHanded
    }

    override fun observeLeftHandedData(): Flow<Boolean> = leftHandedModeFlow
}