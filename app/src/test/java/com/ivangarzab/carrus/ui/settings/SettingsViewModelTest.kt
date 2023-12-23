package com.ivangarzab.carrus.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.repositories.TestAlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.TestAlarmsRepository
import com.ivangarzab.carrus.data.repositories.TestAppSettingsRepository
import com.ivangarzab.carrus.data.repositories.TestCarRepository
import com.ivangarzab.carrus.util.managers.CarExporter
import com.ivangarzab.carrus.util.managers.CarImporter
import com.ivangarzab.test_data.CAR_TEST
import com.ivangarzab.test_data.MainDispatcherRule
import com.ivangarzab.test_data.SERVICE_TEST_1
import com.ivangarzab.test_data.getOrAwaitValue
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.TimeoutException

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SettingsViewModel

    private val carRepository: TestCarRepository = TestCarRepository()
    private val appSettingsRepository: TestAppSettingsRepository = TestAppSettingsRepository()
    private val alarmSettingsRepository: TestAlarmSettingsRepository = TestAlarmSettingsRepository()
    private val alarmsRepository: TestAlarmsRepository = TestAlarmsRepository()

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            carRepository = carRepository,
            appSettingsRepository = appSettingsRepository,
            alarmsRepository = alarmsRepository,
            alarmSettingsRepository = alarmSettingsRepository,
            analytics = mockk(relaxUnitFun = true),
            mockk(),
            CarExporter(),
            CarImporter()
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
        saveCarData()
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onDeleteCarDataClicked()
            }
            assertThat(result.isThereCarData)
                .isFalse()
        }
    }

    @Test
    fun test_onDeleteServicesClicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.isThereCarServicesData)
            .isFalse()
    }

    @Test
    fun test_onDeleteServicesClicked_call_without_services() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onDeleteServicesClicked()
        }
        assertThat(result.isThereCarServicesData)
            .isFalse()
    }

    @Test
    fun test_onDeleteServicesClicked_with_services() = runTest {
        with(viewModel) {
            saveCarData()
            addService()
            assertThat(carRepository.carDataFlow.value?.services?.isNotEmpty())
                .isTrue()

            val result = state.getOrAwaitValue {
                onDeleteServicesClicked()
            }
            assertThat(result.isThereCarServicesData)
                .isFalse()
        }
    }

    @Test
    fun test_onAlarmsToggled_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.alarmsOn)
            .isFalse()
    }

    @Test
    fun test_onAlarmsToggled_updated_false() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onAlarmsToggled(true)
        }
        assertThat(result.alarmsOn)
            .isFalse()
    }

    @Test
    fun test_onAlarmsToggled_updated_true_without_permissionGranted() = runTest {
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onAlarmsToggled(true)
            }
            assertThat(result.alarmsOn)
                .isFalse()
        }
    }

    @Test
    fun test_onAlarmsToggled_updated_true_with_permissionGranted() = runTest {
        with(viewModel) {
            grantAlarmsPermission()
            val result = state.getOrAwaitValue {
                onAlarmsToggled(true)
            }
            assertThat(result.alarmsOn)
                .isTrue()
        }
    }

    @Test(expected = TimeoutException::class)
    fun test_onAlarmsToggled_onRequestAlarmPermission_base() = runTest {
        with(viewModel) {
            grantAlarmsPermission()
            val result = onRequestAlarmPermission.getOrAwaitValue {
                onAlarmsToggled(true)
            }
            assertThat(result)
                .isNotNull()
        }
    }

    @Test
    fun test_onAlarmsToggled_onRequestAlarmPermission_triggered() = runTest {
        with(viewModel) {
            val result = onRequestAlarmPermission.getOrAwaitValue {
                onAlarmsToggled(true)
            }
            assertThat(result)
                .isNotNull()
        }
    }

    @Test
    fun test_onAlarmTimePicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.alarmTime)
            .isEqualTo(AlarmTime.default)
    }

    @Test
    fun test_onAlarmTimePicked_state_updated() = runTest {
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onAlarmTimePicked(TEST_ALARM_TIME_VALUE)
            }
            assertThat(result.alarmTime)
                .isEqualTo(AlarmTime(TEST_ALARM_TIME_VALUE))
        }
    }

    @Test
    fun test_onAlarmFrequencyPicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.alarmFrequency)
            .isEqualTo(AlarmFrequency.DAILY)
    }

    @Test
    fun test_onAlarmFrequencyPicked_updated() = runTest {
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onAlarmFrequencyPicked(AlarmFrequency.WEEKLY)
            }
            assertThat(result.alarmFrequency)
                .isEqualTo(AlarmFrequency.WEEKLY)
        }
    }

    @Test
    fun test_onDueDateFormatPicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.dueDateFormat)
            .isEqualTo(DueDateFormat.DATE)
    }

    @Test
    fun test_onDueDateFormatPicked_ba() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onDueDateFormatPicked("days")
        }
        assertThat(result.dueDateFormat)
            .isEqualTo(DueDateFormat.DAYS)
    }

    @Test
    fun test_onClockTimeFormatPicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.clockTimeFormat)
            .isEqualTo(TimeFormat.HR12)
    }

    @Test
    fun test_onClockTimeFormatPicked_updated() = runTest {
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onClockTimeFormatPicked(TimeFormat.HR24.value)
            }
            assertThat(result.clockTimeFormat)
                .isEqualTo(TimeFormat.HR24)
        }
    }

    private fun addService() {
        carRepository.addCarService(SERVICE_TEST_1)
    }

    private fun saveCarData() {
        carRepository.saveCarData(CAR_TEST)
    }

    private fun grantAlarmsPermission() {
        alarmSettingsRepository.setIsAlarmPermissionGranted(true)
    }

    companion object {
        private const val TEST_ALARM_TIME_VALUE = 6
    }
}