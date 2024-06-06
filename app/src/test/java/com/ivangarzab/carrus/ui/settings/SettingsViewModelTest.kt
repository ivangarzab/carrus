package com.ivangarzab.carrus.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.data.alarm.AlarmTime
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.providers.DebugFlagProviderImpl
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepositoryImpl
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.TestAlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.TestAlarmsRepository
import com.ivangarzab.carrus.data.repositories.TestCarRepository
import com.ivangarzab.carrus.data.states.AppSettingsState
import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.managers.CarExporter
import com.ivangarzab.carrus.util.managers.CarImporter
import com.ivangarzab.carrus.util.managers.NightThemeManager
import com.ivangarzab.test_data.CAR_TEST
import com.ivangarzab.test_data.MainDispatcherRule
import com.ivangarzab.test_data.SERVICE_TEST_1
import com.ivangarzab.test_data.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
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

    val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)

    private val carRepository: TestCarRepository = TestCarRepository()
    private val appSettingsRepository: AppSettingsRepository = AppSettingsRepositoryImpl(mockDataStore)
    private val alarmSettingsRepository: TestAlarmSettingsRepository = TestAlarmSettingsRepository()
    private val alarmsRepository: TestAlarmsRepository = TestAlarmsRepository()
    private val nightThemeManager: NightThemeManager = mockk(relaxUnitFun = true)
    private lateinit var carImporter: CarImporter
    private lateinit var carExporter: CarExporter
    private lateinit var contentResolverHelper: ContentResolverHelper

    @Before
    fun setup() {
        coEvery { mockDataStore.data} returns flow { AppSettingsState() }

        carImporter = mockk()
        carExporter = mockk()
        contentResolverHelper = mockk(relaxUnitFun = true)
        viewModel = getViewModel()
    }

    @Test
    fun test_onDarkModeToggleClicked_true() {
        coEvery { nightThemeManager.fetchNightThemeSetting() } returns true

        viewModel.onDarkModeToggleClicked(true)
        runTest {
            assertThat(nightThemeManager.fetchNightThemeSetting())
                .isTrue()
        }
    }

    @Test
    fun test_onDarkModeToggleClicked_false() {
        coEvery { nightThemeManager.fetchNightThemeSetting() } returns false

        viewModel.onDarkModeToggleClicked(false)
        runTest {
            assertThat(nightThemeManager.fetchNightThemeSetting())
                .isFalse()
        }
    }

    @Test
    fun test_onDeleteCarDataClicked_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.isThereCarData)
            .isFalse()
    }

    @Test
    fun test_onDeleteCarDataClicked_with_data() {
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
    fun test_onAlarmsToggled_updated_true_without_permissionGranted() {
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
    fun test_onAlarmTimePicked_state_updated() {
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
            .isEqualTo(DueDateFormat.DAYS)
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
        viewModel = getViewModel()
        assertThat(result.clockTimeFormat)
            .isEqualTo(TimeFormat.HR24)
    }

    @Test
    fun test_onClockTimeFormatPicked_updated() {
        viewModel = getViewModel()
        with(viewModel) {
            val result = state.getOrAwaitValue {
                onClockTimeFormatPicked(TimeFormat.HR24.value)
            }
            assertThat(result.clockTimeFormat)
                .isEqualTo(TimeFormat.HR24)
        }
    }

    @Test
    fun test_onImportData_contentResolver_return_null() {
        every { contentResolverHelper.readFromFile(any()) } returns null

        val result = viewModel.onImportData(mockk())
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_onImportData_carImporter_return_null() {
        every { contentResolverHelper.readFromFile(any()) } returns "success"
        every { carImporter.importFromJson(any()) } returns null

        val result = viewModel.onImportData(mockk())
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_onImportData_return_true() {
        every { contentResolverHelper.readFromFile(any()) } returns "success"
        every { carImporter.importFromJson(any()) } returns Car.default

        val result = viewModel.onImportData(mockk())
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_onExportData_carRepository_return_false() {
        val mockCarRepository: CarRepository = mockk(relaxed = true)
        viewModel = getViewModel(mockCarRepository)

        every { mockCarRepository.fetchCarData() } returns null

        val result = viewModel.onExportData(mockk())
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_onExportData_carExporter_return_false() {
        val mockCarRepository: CarRepository = mockk(relaxed = true)
        viewModel = getViewModel(mockCarRepository)

        every { mockCarRepository.fetchCarData() } returns Car.default
        every { carExporter.exportToJson(any()) } returns null

        val result = viewModel.onExportData(mockk())
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_onExportData_return_true() {
        val mockCarRepository: CarRepository = mockk(relaxed = true)
        viewModel = getViewModel(mockCarRepository)

        every { mockCarRepository.fetchCarData() } returns Car.default
        every { carExporter.exportToJson(Car.default) } returns "success"

        val result = viewModel.onExportData(mockk())
        assertThat(result)
            .isTrue()
    }

    private fun getViewModel(mockCarRepository: CarRepository? = null): SettingsViewModel {
        return SettingsViewModel(
            carRepository = mockCarRepository ?: carRepository,
            appSettingsRepository = appSettingsRepository,
            alarmsRepository = alarmsRepository,
            alarmSettingsRepository = alarmSettingsRepository,
            analytics = mockk(relaxUnitFun = true),
            debugFlagProvider = DebugFlagProviderImpl().apply { forceDebug = true },
            nightThemeManager = nightThemeManager,
            contentResolverHelper = contentResolverHelper,
            carExporter = carExporter,
            carImporter = carImporter
        )
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