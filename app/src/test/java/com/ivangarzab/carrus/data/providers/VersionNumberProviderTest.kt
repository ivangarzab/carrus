package com.ivangarzab.carrus.data.providers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.BuildConfig
import org.junit.Test

/**
 * The purpose of this class is to test the [VersionNumberProvider] class.
 */
class VersionNumberProviderTest {

    private val provider = VersionNumberProviderImpl()

    private val versionNumber = BuildConfig.VERSION_NAME

    @Test
    fun test_getVersionNumber() {
        assertThat(provider.getVersionNumber())
            .isEqualTo("v$versionNumber")
    }
}