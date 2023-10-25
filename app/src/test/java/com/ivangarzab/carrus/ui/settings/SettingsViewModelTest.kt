package com.ivangarzab.carrus.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.MainDispatcherRule
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.TestAppSettingsRepository
import com.ivangarzab.carrus.data.repositories.TestCarRepository
import com.ivangarzab.carrus.getOrAwaitValue
import io.mockk.mockk
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

    private val carRepository: CarRepository = TestCarRepository()
    private val appSettingsRepository: TestAppSettingsRepository = TestAppSettingsRepository()
    private val alarmSettingsRepository: AlarmSettingsRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            carRepository = carRepository,
            appSettingsRepository = appSettingsRepository,
            alarmsRepository = mockk(),
            alarmSettingsRepository = alarmSettingsRepository
        )
    }

    @Test
    fun test_onDarkModeToggleClicked_true() {
        viewModel.onDarkModeToggleClicked(true)
        assertThat(appSettingsRepository.isNight)
            .isTrue()
    }

    @Test
    fun test_onDarkModeToggleClicked_false() {
        viewModel.onDarkModeToggleClicked(false)
        assertThat(appSettingsRepository.isNight)
            .isFalse()
    }

    @Test
    fun test_onDeleteCarDataClicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.isThereCarData)
            .isFalse()
    }

    @Test
    fun test_onDeleteCarDataClicked_with_data() = runTest {
        carRepository.saveCarData(TEST_CAR)
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onDeleteCarDataClicked()
            }
            assertThat(result.isThereCarData)
                .isFalse()
        }
    }
}