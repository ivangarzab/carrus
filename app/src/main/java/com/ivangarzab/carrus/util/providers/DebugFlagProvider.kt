package com.ivangarzab.carrus.util.providers

import com.ivangarzab.carrus.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface DebugFlagProvider {
    var forceDebug: Boolean
    fun isDebugEnabled(): Boolean
}

class DebugFlagProviderImpl @Inject constructor() : DebugFlagProvider {

    override var forceDebug: Boolean = false
    override fun isDebugEnabled(): Boolean =
        when (forceDebug) {
            true -> true
            false -> BuildConfig.DEBUG
        }
}

@Module
@InstallIn(SingletonComponent::class, ViewModelComponent::class)
abstract class DebugFlagProviderModule {
    @Binds
    abstract fun bindDebugFlagProvider(
        debugFlagProvider: DebugFlagProviderImpl
    ) : DebugFlagProvider
}