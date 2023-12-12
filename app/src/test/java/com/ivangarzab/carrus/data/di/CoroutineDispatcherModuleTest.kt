package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineDispatcherModuleTest {

    private val module: CoroutinesDispatchersModule = CoroutinesDispatchersModule

    @Test
    fun test_providesDefaultDispatcher() {
        val result = module.providesDefaultDispatcher()
        assertThat(result).isNotNull()
        assertThat(result).isSameInstanceAs(Dispatchers.Default)
    }

    @Test
    fun test_providesIoDispatcher() {
        val result = module.providesIoDispatcher()
        assertThat(result).isNotNull()
        assertThat(result).isSameInstanceAs(Dispatchers.IO)
    }

    @Test
    fun test_providesMainDispatcher() {
        val result = module.providesMainDispatcher()
        assertThat(result).isNotNull()
        assertThat(result).isSameInstanceAs(Dispatchers.Main)
    }

    @Test
    fun test_providesMainImmediateDispatcher() {
        val result = module.providesMainImmediateDispatcher()
        assertThat(result).isNotNull()
        assertThat(result).isSameInstanceAs(Dispatchers.Main.immediate)
    }
}