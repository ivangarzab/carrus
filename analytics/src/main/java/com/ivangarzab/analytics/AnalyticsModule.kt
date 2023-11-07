package com.ivangarzab.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Ivan Garza Bermea.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    internal abstract fun bindAnalyticsRepository(
        analyticsRepository: AnalyticsRepositoryImpl
    ) : AnalyticsRepository
}