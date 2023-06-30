package com.ivangarzab.carrus.data.repositories

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.prefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val nightThemeFlow = MutableStateFlow(false)

    private val dueDateFormatFlow = MutableStateFlow(DueDateFormat.DAYS)

    init {
        setNightThemeSetting(
            fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context)
        )
    }

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

    fun getVersionNumber(): String = "v${BuildConfig.VERSION_NAME}"

    fun fetchDueDateFormatSetting(): DueDateFormat = prefs.dueDateFormat

    fun setDueDateFormatSetting(format: DueDateFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        prefs.dueDateFormat = format
    }

    fun observeDueDateFormatData(): Flow<DueDateFormat> = dueDateFormatFlow.asStateFlow()

    private fun updateDueDateFormatFlow(format: DueDateFormat) {
        dueDateFormatFlow.value = format
    }
}