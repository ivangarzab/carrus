package com.ivangarzab.carrus.data.repositories

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
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

    init {
        setNightThemeSetting(
            fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context)
        )
    }

    fun observeNightThemeData(): Flow<Boolean> = nightThemeFlow.asStateFlow()

    fun setNightThemeSetting(isNight: Boolean) {
        Timber.d("Setting night theme to: $isNight")
        prefs.darkMode = isNight
        setAppDefaultNightTheme(isNight)
    }

    private fun setAppDefaultNightTheme(isNight: Boolean) = when (isNight) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun fetchNightThemeSetting(): Boolean? = prefs.darkMode

    private fun getNightThemeSettingFromSystem(context: Context): Boolean =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }.also { result ->
            Timber.i("Night theme set to: $result")
        }
}