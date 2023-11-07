package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepositoryImpl
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
abstract class AlarmsRepositoryModule {

    @Binds
    abstract fun bindAlarmsRepository(
        alarmsRepository: AlarmsRepositoryImpl
    ) : AlarmsRepository
}