package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepositoryImpl
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
abstract class AlarmSettingsRepositoryModule {
    @Binds
    abstract fun bindAlarmSettingsRepository(
        alarmSettingsRepository: AlarmSettingsRepositoryImpl
    ) : AlarmSettingsRepository
}