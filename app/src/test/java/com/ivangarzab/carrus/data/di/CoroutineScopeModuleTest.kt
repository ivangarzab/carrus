package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineScopeModuleTest {

    @Test
    fun test_binding() {
        val result = CoroutinesScopeModule.providesCoroutineScope(Dispatchers.Default)
        assertThat(result).isNotNull()
        assertThat(result).isInstanceOf(CoroutineScope::class.java)
    }
}