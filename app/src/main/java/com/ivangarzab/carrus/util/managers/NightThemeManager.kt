package com.ivangarzab.carrus.util.managers

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import timber.log.Timber

/**
 * The purpose of this class is to manage the night theme state for the application.
 */
interface NightThemeManager {
    fun setNightThemeSetting(isNight: Boolean)
    fun fetchNightThemeSetting(): Boolean?
}

/**
 *
 */
class NightThemeManagerImpl(
    context: Context,
    private val prefs: Preferences
): NightThemeManager {

    init {
        setNightThemeSetting(
            fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context)
        )
    }

    override fun setNightThemeSetting(isNight: Boolean) {
        Timber.d("Setting night theme to: $isNight")
        setAppDefaultNightTheme(isNight)
        prefs.darkMode = isNight
    }

    @VisibleForTesting
    override fun fetchNightThemeSetting(): Boolean? = prefs.darkMode

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

    companion object NightThemePreferences {
        const val NAME = "night-theme-preferences"
        val NIGHT_THEME = booleanPreferencesKey("dark-mode")
    }
}