package com.ivangarzab.carrus.data.di

import android.content.Context
import android.content.SharedPreferences
import com.google.common.truth.Truth
import com.ivangarzab.carrus.util.managers.Preferences
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class PreferencesModuleTest {

    private val context: Context = mockk()

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    @Before
    fun setup() {
        every { context.getSharedPreferences(any(), any()) } returns sharedPreferences
    }

    @Test
    fun test_binding() {
        val result = PreferencesModule.providesPreferences(
            context,
            TestDebugFlagProvider()
        )
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(Preferences::class.java)
    }

    @Test
    fun test_singleton() {
        val result1 = PreferencesModule.providesPreferences(
            context,
            TestDebugFlagProvider()
        )
        val result2 = PreferencesModule.providesPreferences(
            context,
            TestDebugFlagProvider()
        )
        Truth.assertThat(result1)
            .isEqualTo(result2)
    }
}