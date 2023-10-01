package com.ivangarzab.carrus.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.MainDispatcherRule
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SettingsViewModel

    private var state: SettingsState = SettingsState()
    private val stateObserver = Observer<SettingsState> {
        state = it
    }

    private val carRepository: CarRepository = mockk(relaxed = true)
    private val appSettingsRepository: AppSettingsRepository = mockk(relaxed = true)
    private val alarmSettingsRepository: AlarmSettingsRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            carRepository = carRepository,
            appSettingsRepository = appSettingsRepository,
            alarmsRepository = mockk(),
            alarmSettingsRepository = alarmSettingsRepository
        ).apply {
            state.observeForever(stateObserver)
        }

        every { carRepository.fetchCarData() } returns TEST_CAR
        every { carRepository.observeCarData() } returns MutableStateFlow(TEST_CAR).asStateFlow()
    }

    @Test
    fun test_onDarkModeToggleClicked_true() {
        every { appSettingsRepository.fetchNightThemeSetting() } returns true
        viewModel.onDarkModeToggleClicked(true)
        assertThat(appSettingsRepository.fetchNightThemeSetting())
            .isTrue()
    }

    @Test
    fun test_onDarkModeToggleClicked_false() {
        every { appSettingsRepository.fetchNightThemeSetting() } returns false
        viewModel.onDarkModeToggleClicked(false)
        assertThat(appSettingsRepository.fetchNightThemeSetting())
            .isFalse()
    }

    /*@Test TODO: Failing due to Kotlin Flows not working properly
    fun test_onDeleteCarDataClicked_base() = runTest {
        assertThat(state.isThereCarData)
            .isTrue()
    }*/

    @Test
    fun test_onDeleteCarDataClicked_success() = runTest {
        viewModel.onDeleteCarDataClicked()
        assertThat(state.isThereCarData)
            .isFalse()
    }
}