package com.ivangarzab.carrus.data.repositories

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.data.DueDateFormat
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
        prefs.dueDateFormat = DueDateFormat.DAYS
    }

    @Test
    fun test_setNightThemeSetting_base() {
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
        setNightThemeSetting(false)
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

    @Test
    fun test_getVersionNumber() = with(repository) {
        assertThat(getVersionNumber())
            .matches("v${BuildConfig.VERSION_NAME}")
    }

    @Test
    fun test_setDueDateFormatSetting_base() {
        assertThat(prefs.dueDateFormat)
            .isSameInstanceAs(DueDateFormat.DAYS)
    }

    @Test
    fun test_setDueDateFormatSetting_months_success() = with(repository) {
        setDueDateFormatSetting(DueDateFormat.MONTHS)
        assertThat(prefs.dueDateFormat)
            .isSameInstanceAs(DueDateFormat.MONTHS)
    }

    @Test
    fun test_setDueDateFormatSetting_months_fail() = with(repository) {
        setDueDateFormatSetting(DueDateFormat.MONTHS)
        assertThat(prefs.dueDateFormat)
            .isNotSameInstanceAs(DueDateFormat.WEEKS)
    }

    @Test
    fun test_fetchDueDateFormatSetting_days_base() = with(repository) {
        assertThat(fetchDueDateFormatSetting())
            .isSameInstanceAs(DueDateFormat.DAYS)
    }

    @Test
    fun test_fetchDueDateFormatSetting_months_success() = with(repository) {
        setDueDateFormatSetting(DueDateFormat.MONTHS)
        assertThat(fetchDueDateFormatSetting())
            .isSameInstanceAs(DueDateFormat.MONTHS)
    }
}