package com.ivangarzab.carrus.util.managers

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * The purpose of this class is to manage the night theme state for the application.
 */
class NightThemeManager(
    @get:VisibleForTesting val dataStore: DataStore<Preferences>,
    context: Context,
    coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch(Dispatchers.IO) {
            setNightThemeSetting(
                fetchNightThemeSetting() ?: getNightThemeSettingFromSystem(context)
            )
        }
    }

    suspend fun setNightThemeSetting(isNight: Boolean) {
        Timber.d("Setting night theme to: $isNight")
        setAppDefaultNightTheme(isNight)
        dataStore.edit { preferences ->
            preferences[NIGHT_THEME] = isNight
        }
    }

    @VisibleForTesting
    suspend fun fetchNightThemeSetting(): Boolean? = dataStore.data.firstOrNull()?.get(NIGHT_THEME)

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