package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineScopeModuleTest {

    @Test
    fun test_binding() {
        val result = CoroutineScopeModule.providesCoroutineScope(Dispatchers.Default)
        assertThat(result).isNotNull()
        assertThat(result).isInstanceOf(CoroutineScope::class.java)
    }

    @Test
    fun test_singleton() {
        val result1 = CoroutineScopeModule.providesCoroutineScope(Dispatchers.Default)
        val result2 = CoroutineScopeModule.providesCoroutineScope(Dispatchers.Default)
        assertThat(result1)
            .isEqualTo(result2)
    }
}