package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.util.managers.CarExporter
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
object CarExporterModule {
    private lateinit var instance: CarExporter
    @Singleton
    @Provides
    fun provideCarExporter(): CarExporter {
        if (this::instance.isInitialized.not()) {
            instance = CarExporter()
        }
        return instance
    }
}