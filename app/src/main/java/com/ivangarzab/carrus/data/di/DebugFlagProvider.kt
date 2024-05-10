package com.ivangarzab.carrus.data.di

import com.ivangarzab.carrus.BuildConfig

interface DebugFlagProvider {
    var forceDebug: Boolean
    fun isDebugEnabled(): Boolean
}

class DebugFlagProviderImpl : DebugFlagProvider {

    override var forceDebug: Boolean = false
    override fun isDebugEnabled(): Boolean =
        when (forceDebug) {
            true -> true
            false -> BuildConfig.DEBUG
        }
}
