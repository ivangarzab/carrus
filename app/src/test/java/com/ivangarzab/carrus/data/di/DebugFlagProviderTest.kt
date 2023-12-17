package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth
import com.ivangarzab.carrus.BuildConfig
import org.junit.Before
import org.junit.Test
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class DebugFlagProviderTest {

    private lateinit var testProvider: TestDebugFlagProvider

    private lateinit var implProvider: DebugFlagProviderImpl

    @Before
    fun setup() {
        testProvider = TestDebugFlagProvider()
        implProvider = DebugFlagProviderImpl()
    }

    @Test
    fun test_forceDebug_base_from_test_class() {
        val result = testProvider.forceDebug
        Truth.assertThat(result)
            .isEqualTo(FALSE)
    }

    @Test
    fun test_forceDebug_true_from_test_class() {
        testProvider.forceDebug = true
        val result = testProvider.forceDebug
        Truth.assertThat(result)
            .isEqualTo(TRUE)
    }

    @Test
    fun test_isDebugEnabled_base_from_test_class() {
        val result = testProvider.isDebugEnabled()
        Truth.assertThat(result)
            .isEqualTo(testProvider.internalFlag)
    }

    @Test
    fun test_isDebugEnabled_true_from_test_class() {
        testProvider.internalFlag = true
        val result = testProvider.isDebugEnabled()
        Truth.assertThat(result)
            .isEqualTo(TRUE)
    }

    @Test
    fun test_forceDebug_base_from_impl_class() {
        val result = implProvider.forceDebug
        Truth.assertThat(result)
            .isEqualTo(FALSE)
    }

    @Test
    fun test_forceDebug_true_from_impl_class() {
        implProvider.forceDebug = true
        val result = implProvider.forceDebug
        Truth.assertThat(result)
            .isEqualTo(TRUE)
    }

    @Test
    fun test_isDebugEnabled_base_from_impl_class() {
        val result = implProvider.isDebugEnabled()
        Truth.assertThat(result)
            .isEqualTo(BuildConfig.DEBUG)
    }

    @Test
    fun test_isDebugEnabled_base_with_forceDebug_true_from_impl_class() {
        implProvider.forceDebug = true
        val result = implProvider.isDebugEnabled()
        Truth.assertThat(result)
            .isEqualTo(BuildConfig.DEBUG)
    }

    @Test
    fun test_module_bind() {
        val module = object : DebugFlagProviderModule() {
            override fun bindDebugFlagProvider(debugFlagProvider: DebugFlagProviderImpl): DebugFlagProvider {
                return implProvider
            }

        }
        val provider = module.bindDebugFlagProvider(implProvider)
        val result = provider.isDebugEnabled()
        Truth.assertThat(result)
            .isEqualTo(BuildConfig.DEBUG)
    }
}