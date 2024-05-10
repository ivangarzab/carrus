package com.ivangarzab.carrus.data.repositories

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.providers.DebugFlagProviderImpl
import com.ivangarzab.carrus.util.managers.Preferences
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepositoryTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    private val prefs: Preferences = Preferences(
        context,
        DebugFlagProviderImpl().apply { forceDebug = true }
    )

    private val repository = AppSettingsRepositoryImpl(context, prefs)

    @Before
    fun setup() {
        prefs.darkMode = null
        prefs.dueDateFormat = DueDateFormat.DAYS
        prefs.timeFormat = TimeFormat.HR12
        prefs.leftHandedMode = false
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

    @Test
    fun test_setTimeFormatSetting_base() {
        assertThat(prefs.timeFormat)
            .isSameInstanceAs(TimeFormat.HR12)
    }

    @Test
    fun test_setTimeFormatSetting_hr24_success() = with(repository) {
        setTimeFormatSetting(TimeFormat.HR24)
        assertThat(prefs.timeFormat)
            .isSameInstanceAs(TimeFormat.HR24)
    }

    @Test
    fun test_setTimeFormatSetting_hr24_fail() = with(repository) {
        setTimeFormatSetting(TimeFormat.HR24)
        assertThat(prefs.timeFormat)
            .isNotSameInstanceAs(TimeFormat.HR12)
    }

    @Test
    fun test_fetchTimeFormatSetting_hr12_base() = with(repository) {
        assertThat(fetchTimeFormatSetting())
            .isSameInstanceAs(TimeFormat.HR12)
    }

    @Test
    fun test_fetchTimeFormatSetting_hr24_success() = with(repository) {
        setTimeFormatSetting(TimeFormat.HR24)
        assertThat(fetchTimeFormatSetting())
            .isSameInstanceAs(TimeFormat.HR24)
    }

    @Test
    fun test_setLeftHandedSetting_base() =
        assertThat(prefs.leftHandedMode)
            .isFalse()

    @Test
    fun test_setLeftHandedSetting_true() = with(repository) {
        setLeftHandedSetting(true)
        assertThat(prefs.leftHandedMode)
            .isTrue()
    }

    @Test
    fun test_setLeftHandedSetting_false() = with(repository) {
        setLeftHandedSetting(false)
        assertThat(prefs.leftHandedMode)
            .isFalse()
    }

    @Test
    fun test_fetchLeftHandedSettings_true() = with(repository) {
        setLeftHandedSetting(true)
        assertThat(fetchLeftHandedSetting())
            .isTrue()
    }

    @Test
    fun test_fetchLeftHandedSettings_false() = with(repository) {
        setLeftHandedSetting(false)
        assertThat(fetchLeftHandedSetting())
            .isFalse()
    }
}