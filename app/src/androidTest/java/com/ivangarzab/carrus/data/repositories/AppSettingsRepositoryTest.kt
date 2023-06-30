package com.ivangarzab.carrus.data.repositories

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.prefs
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepositoryTest {

    private val repository = AppSettingsRepository(
        InstrumentationRegistry.getInstrumentation().context
    )

    @Before
    fun setup() {
        prefs.darkMode = null
    }

    @Test
    fun test_base() {
        assertThat(prefs.darkMode)
            .isNull()
    }

    @Test
    fun test_setNightThemeSetting_true() = with(repository) {
        setNightThemeSetting(true)
        assertThat(prefs.darkMode)
            .isTrue()
    }

    @Test
    fun test_setNightThemeSetting_false() = with(repository) {
        setNightThemeSetting(true)
        assertThat(prefs.darkMode)
            .isFalse()
    }

    @Test
    fun test_fetchNightThemeSettings_null_base() = with(repository) {
        assertThat(fetchNightThemeSetting())
            .isNull()
    }

    @Test
    fun test_fetchNightThemeSettings_true() = with(repository) {
        setNightThemeSetting(true)
        assertThat(fetchNightThemeSetting())
            .isTrue()
    }

    @Test
    fun test_fetchNightThemeSettings_false() = with(repository) {
        setNightThemeSetting(false)
        assertThat(fetchNightThemeSetting())
            .isFalse()
    }
}