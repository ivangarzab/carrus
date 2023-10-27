package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.repositories.TestAppSettingsRepository.Companion.DEFAULT_VERSION_NUMBER
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AppSettingsRepositoryTest {

    private lateinit var repository: TestAppSettingsRepository

    @Before
    fun setup() {
        repository = TestAppSettingsRepository()
        repository._appSettingsStateFlow = MutableStateFlow(AppSettingsState())
    }

    @Test
    fun test_setNightThemeSetting_true() = with(repository) {
        setNightThemeSetting(true)
        assertThat(isNight)
            .isTrue()
    }

    @Test
    fun test_setNightThemeSetting_false() = with(repository) {
        setNightThemeSetting(false)
        assertThat(isNight)
            .isFalse()
    }

    @Test
    fun test_fetchNightThemeSettings_null_base() = with(repository) {
        assertThat(isNight)
            .isNull()
    }

    @Test
    fun test_fetchNightThemeSettings_true() = with(repository) {
        setNightThemeSetting(true)
        assertThat(isNight)
            .isTrue()
    }

    @Test
    fun test_fetchNightThemeSettings_false() = with(repository) {
        setNightThemeSetting(false)
        assertThat(isNight)
            .isFalse()
    }

    @Test
    fun test_getVersionNumber() = with(repository) {
        assertThat(getVersionNumber())
            .matches(DEFAULT_VERSION_NUMBER)
    }

    @Test
    fun test_setDueDateFormatSetting_base() = runTest {
        val result = repository.observeAppSettingsStateData().first()
        assertThat(result.dueDateFormat)
            .isSameInstanceAs(DueDateFormat.DATE)
    }

    @Test
    fun test_setDueDateFormatSetting_months_success() = runTest {
        with(repository) {
            setDueDateFormatSetting(DueDateFormat.MONTHS)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.dueDateFormat)
                .isSameInstanceAs(DueDateFormat.MONTHS)
        }
    }

    @Test
    fun test_setDueDateFormatSetting_months_fail() = runTest {
        with(repository) {
            setDueDateFormatSetting(DueDateFormat.MONTHS)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.dueDateFormat)
                .isNotSameInstanceAs(DueDateFormat.WEEKS)
        }
    }

    @Test
    fun test_fetchDueDateFormatSetting_months_success() = runTest {
        with(repository) {
            setDueDateFormatSetting(DueDateFormat.MONTHS)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.dueDateFormat)
                .isSameInstanceAs(DueDateFormat.MONTHS)
        }
    }

    @Test
    fun test_setTimeFormatSetting_base() = runTest {
        val result = repository.observeAppSettingsStateData().first()
        assertThat(result.timeFormat)
            .isSameInstanceAs(TimeFormat.HR12)
    }

    @Test
    fun test_setTimeFormatSetting_hr24_success() = runTest {
        with(repository) {
            setTimeFormatSetting(TimeFormat.HR24)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.timeFormat)
                .isSameInstanceAs(TimeFormat.HR24)
        }
    }

    @Test
    fun test_setTimeFormatSetting_hr24_fail() = runTest {
        with(repository) {
            setTimeFormatSetting(TimeFormat.HR24)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.timeFormat)
                .isNotSameInstanceAs(TimeFormat.HR12)
        }
    }

    @Test
    fun test_fetchTimeFormatSetting_hr12_base() = runTest {
        with(repository) {
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.timeFormat)
                .isSameInstanceAs(TimeFormat.HR12)
        }
    }

    @Test
    fun test_fetchTimeFormatSetting_hr24_success() = runTest {
        with(repository) {
            setTimeFormatSetting(TimeFormat.HR24)
            val result = repository.observeAppSettingsStateData().first()
            assertThat(result.timeFormat)
                .isSameInstanceAs(TimeFormat.HR24)
        }
    }

    @Test
    fun test_setLeftHandedSetting_base() =
        assertThat(repository.isLefty)
            .isFalse()

    @Test
    fun test_setLeftHandedSetting_true() = with(repository) {
        setLeftHandedSetting(true)
        assertThat(isLefty)
            .isTrue()
    }

    @Test
    fun test_setLeftHandedSetting_false() = with(repository) {
        setLeftHandedSetting(false)
        assertThat(isLefty)
            .isFalse()
    }

    @Test
    fun test_fetchLeftHandedSettings_true() = with(repository) {
        setLeftHandedSetting(true)
        assertThat(isLefty)
            .isTrue()
    }

    @Test
    fun test_fetchLeftHandedSettings_false() = with(repository) {
        setLeftHandedSetting(false)
        assertThat(isLefty)
            .isFalse()
    }
}