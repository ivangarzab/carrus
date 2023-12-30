package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.util.managers.AlarmScheduler
import com.ivangarzab.carrus.util.managers.AlarmSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmSchedulerModule {

    @Binds
    abstract fun bindAlarmScheduler(
        alarmScheduler: AlarmSchedulerImpl
    ) : AlarmScheduler
}