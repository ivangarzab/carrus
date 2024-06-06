package com.ivangarzab.carrus.data.providers

import com.ivangarzab.carrus.BuildConfig

/**
 * The purpose of this class is to provide the application's version number.
 */
interface VersionNumberProvider {
    /**
     * Returns the application's version number as a String.
     *
     * @return The app's version number.
     */
    fun getVersionNumber(): String
}

/**
 * The purpose of this class is to serve as the implementation of [VersionNumberProvider].
 */
class VersionNumberProviderImpl : VersionNumberProvider {
    override fun getVersionNumber(): String = "v${BuildConfig.VERSION_NAME}"
}