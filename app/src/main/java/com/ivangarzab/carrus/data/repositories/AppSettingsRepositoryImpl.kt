package com.ivangarzab.carrus.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppSettingsRepository {

    override val appSettingsFlow: Flow<AppSettingsState> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val timeFormat = TimeFormat.get(preferences[TIME_FORMAT] ?: TimeFormat.DEFAULT.name)
            val dueDateFormat = DueDateFormat.get(
                preferences[DUE_DATE_FORMAT] ?: DueDateFormat.DEFAULT.name
            )
            val leftHandedMode = preferences[LEFT_HANDED_MODE] ?: false
            AppSettingsState(timeFormat, dueDateFormat, leftHandedMode)
        }

    //TODO: Convert into provider class
    // override fun getVersionNumber(): String = "v${BuildConfig.VERSION_NAME}"

//    override fun observeAppSettingsStateData(): Flow<AppSettingsState> = appSettingsStateFlow

    /** Night Theme/Dark Mode **/
    //TODO:
    // @VisibleForTesting
    // fun fetchNightThemeSetting(): Boolean? = prefs.darkMode

    //TODO: Consider moving night theme code to a separate class
//    override fun setNightThemeSetting(isNight: Boolean) {
//        Timber.d("Setting night theme to: $isNight")
//        prefs.darkMode = isNight
//        setAppDefaultNightTheme(isNight)
//    }

    /*private fun setAppDefaultNightTheme(isNight: Boolean) = when (isNight) {
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
        }*/

    override suspend fun setDueDateFormatSetting(format: DueDateFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        dataStore.edit { preferences ->
            preferences[DUE_DATE_FORMAT] = format.value
        }
    }

    override suspend fun setTimeFormatSetting(format: TimeFormat) {
        Timber.v("Setting due date format setting: ${format.value}")
        dataStore.edit { preferences ->
            preferences[TIME_FORMAT] = format.value
        }
    }

    override suspend fun setLeftHandedSetting(isLeftHanded: Boolean) {
        dataStore.edit { preferences ->
            preferences[LEFT_HANDED_MODE] = isLeftHanded
        }
    }

    companion object AppSettingsPreferences {
        const val NAME = "app-settings-preferences"
        val TIME_FORMAT = stringPreferencesKey("format-time")
        val DUE_DATE_FORMAT = stringPreferencesKey("format-due-date")
        val LEFT_HANDED_MODE = booleanPreferencesKey("left-handed-mode")
    }
}