package com.ivangarzab.carrus.util.managers

import android.content.Context
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this file is to test the [NightThemeManager] class.
 */
class NightThemeManagerTest {

    val mockContext: Context = mockk(relaxed = true)

    val mockPrefs: Preferences = mockk(relaxed = true)

    private lateinit var nightThemeManager: NightThemeManager

    @Before
    fun setup() {
        nightThemeManager = NightThemeManagerImpl(
            prefs = mockPrefs,
            context = mockContext,
        )
    }

    @Test
    fun test_fetchNightThemeSetting_true() = runTest {
        every { mockPrefs.darkMode } returns true
        nightThemeManager.setNightThemeSetting(true)
        val result = nightThemeManager.fetchNightThemeSetting()
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_fetchNightThemeSetting_false() = runTest {
        every { mockPrefs.darkMode } returns false
        nightThemeManager.setNightThemeSetting(false)
        val result = nightThemeManager.fetchNightThemeSetting()
        assertThat(result)
            .isFalse()
    }
}