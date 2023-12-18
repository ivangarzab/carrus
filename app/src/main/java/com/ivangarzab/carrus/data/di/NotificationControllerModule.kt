package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.util.managers.NotificationController
import com.ivangarzab.carrus.util.managers.NotificationControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationControllerModule {
    @Binds
    abstract fun bindNotificationController(
        notificationController: NotificationControllerImpl
    ) : NotificationController
}