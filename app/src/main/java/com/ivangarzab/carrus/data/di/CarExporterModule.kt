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
object CarExporterModule : SingletonModule<CarExporter>() {
    @Singleton
    @Provides
    fun provideCarExporter(): CarExporter {
        checkForInstanceAndCreateAsNeeded(CarExporter::class.java)
        return instance as CarExporter
    }
}