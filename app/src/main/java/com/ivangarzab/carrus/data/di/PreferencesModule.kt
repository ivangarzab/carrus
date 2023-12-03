package com.ivangarzab.carrus.data.di

import android.content.Context
import com.ivangarzab.carrus.util.managers.Preferences
import com.ivangarzab.carrus.util.providers.DebugFlagProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {
    private lateinit var instance: Preferences
    @Singleton
    @Provides
    fun providesPreferences(
        @ApplicationContext context: Context,
        debugFlagProvider: DebugFlagProvider
    ): Preferences {
        if (this::instance.isInitialized.not()) {
            instance = Preferences(context, debugFlagProvider)
        }
        return instance
    }
}