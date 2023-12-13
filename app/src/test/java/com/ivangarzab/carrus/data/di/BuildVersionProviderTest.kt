package com.ivangarzab.carrus.data.di

import android.os.Build
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.di.TestBuildVersionProvider.Companion.TEST_SDK_VERSION
import org.junit.Test

class BuildVersionProviderTest {

    private val implProvider = BuildVersionProviderImpl()

    @Test
    fun test_getSdkVersionInt_from_test_class() {
        val provider = TestBuildVersionProvider()
        val result = provider.getSdkVersionInt()
        assertThat(result)
            .isEqualTo(TEST_SDK_VERSION)
    }

    @Test
    fun test_getSdkVersionInt_from_impl_class() {
        val result = implProvider.getSdkVersionInt()
        assertThat(result)
            .isEqualTo(Build.VERSION.SDK_INT)
    }

    @Test
    fun test_module_bind() {
        val module = object : BuildVersionProviderModule() {
            override fun bindBuildVersionProvider(buildVersionProvider: BuildVersionProviderImpl): BuildVersionProvider {
                return implProvider
            }
        }
        val provider = module.bindBuildVersionProvider(implProvider)
        val result = provider.getSdkVersionInt()
        assertThat(result)
            .isEqualTo(Build.VERSION.SDK_INT)
    }
}