package com.ivangarzab.carrus.data.repositories

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.prefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class AppSettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    data class AppSettingsState(
        val timeFormat: TimeFormat = TimeFormat.HR12,
        val dueDateFormat: DueDateFormat = DueDateFormat.DATE
    )
    private val appSettingsStateFlow = MutableStateFlow(AppSettingsState())

    private val nightThemeFlow = MutableStateFlow(false)

    private val leftHandedModeFlow = MutableStateFlow(false)

    init {
        setNightThemeSetting(fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context))
        setDueDateFormatSetting(fetchDueDateFormatSetting())
        setTimeFormatSetting(fetchTimeFormatSetting())
        setLeftHandedSetting(fetchLeftHandedSetting())
    }

    fun getVersionNumber(): String = "v${BuildConfig.VERSION_NAME}"

    fun observeAppSettingsStateData(): Flow<AppSettingsState> = appSettingsStateFlow.asStateFlow()

    /** Night Theme/Dark Mode **/
//TODO: I may not need this info after the Compose migration after all
    fun fetchNightThemeSetting(): Boolean? = prefs.darkMode

    fun setNightThemeSetting(isNight: Boolean) {
        Timber.d("Setting night theme to: $isNight")
        prefs.darkMode = isNight
        setAppDefaultNightTheme(isNight)
    }

    fun observeNightThemeData(): Flow<Boolean> = nightThemeFlow.asStateFlow()

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

    fun fetchDueDateFormatSetting(): DueDateFormat = prefs.dueDateFormat

    fun setDueDateFormatSetting(format: DueDateFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        prefs.dueDateFormat = format
        updateDueDateFormatFlow(format)
    }

    private fun updateDueDateFormatFlow(format: DueDateFormat) {
        appSettingsStateFlow.value.let {
            appSettingsStateFlow.value = it.copy(
                dueDateFormat = format
            )
        }
    }

    /** Time Format functions **/

    fun fetchTimeFormatSetting(): TimeFormat = prefs.timeFormat.also {
        Timber.v("Got time format: ${it.name}")
    }

    fun setTimeFormatSetting(format: TimeFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        prefs.timeFormat = format
        updateTimeFormatFlow(format)
    }

    private fun updateTimeFormatFlow(format: TimeFormat) {
        appSettingsStateFlow.value.let {
            appSettingsStateFlow.value = it.copy(
                timeFormat = format
            )
        }
    }

    /** Left-handed Mode functions **/

    fun fetchLeftHandedSetting(): Boolean = prefs.leftHandedMode

    fun setLeftHandedSetting(isLeftHanded: Boolean) {
        Timber.v("Setting left-handed mode: $isLeftHanded")
        prefs.leftHandedMode = isLeftHanded
    }

    fun observeLeftHandedData(): Flow<Boolean> = leftHandedModeFlow.asStateFlow()
}