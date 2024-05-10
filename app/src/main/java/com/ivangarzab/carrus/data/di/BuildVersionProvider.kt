package com.ivangarzab.carrus.data.di

import android.os.Build
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
interface BuildVersionProvider {
    fun getSdkVersionInt(): Int
}

class BuildVersionProviderImpl @Inject constructor() : BuildVersionProvider {
    override fun getSdkVersionInt(): Int {
        return Build.VERSION.SDK_INT
    }
}

/*
@Module
@InstallIn(ViewModelComponent::class)
abstract class BuildVersionProviderModule {
    @Binds
    abstract fun bindBuildVersionProvider(
        buildVersionProvider: BuildVersionProviderImpl
    ) : BuildVersionProvider
}*/
