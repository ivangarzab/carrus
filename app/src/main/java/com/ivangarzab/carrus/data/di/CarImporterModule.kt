package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.util.managers.CarImporter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Module
@InstallIn(SingletonComponent::class)
object CarImporterModule {
    private lateinit var instance: CarImporter
    @Singleton
    @Provides
    fun provideCarImporter(): CarImporter {
        if (this::instance.isInitialized.not()) {
            instance = CarImporter()
        }
        return instance
    }
}