package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

/**
 * Created by Ivan Garza Bermea.
 */
@Module
@InstallIn(SingletonComponent::class, ViewModelComponent::class)
abstract class AppSettingsRepositoryModule {
    @Binds
    abstract fun bindAppSettingsRepository(
        appSettingsRepository: AppSettingsRepositoryImpl
    ) : AppSettingsRepository
}