package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.helpers.ContentResolverHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Ivan Garza Bermea.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ContentResolverHelperModule {

    @Binds
    abstract fun bindContentResolverHelper(
        contentResolverHelper: ContentResolverHelperImpl
    ) : ContentResolverHelper
}