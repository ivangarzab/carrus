package com.ivangarzab.carrus.data.datastore

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import com.ivangarzab.carrus.data.repositories.AppSettingsRepositoryImpl
import com.ivangarzab.carrus.util.managers.Preferences

/**
 * The purpose of this class is to hold all of the [DataStore] instances.
 */

val Context.appSettingsDataStore by preferencesDataStore(
    name = AppSettingsRepositoryImpl.NAME,
    produceMigrations = { context ->
        // SharedPreferences migration code -- TODO: Remove in 2 versions or so
        listOf(SharedPreferencesMigration(context, Preferences.DEFAULT_SHARED_PREFS))
    }
)