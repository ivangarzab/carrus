package com.ivangarzab.carrus.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsRepositoryTest {

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFolder.newFile("$DATA_STORE_NAME.preferences_pb") }
    )

    private lateinit var repository: AppSettingsRepository

    @Before
    fun setup() {
        repository = AppSettingsRepositoryImpl(testDataStore)
    }

    @Test()
    fun test_flow_catch_empty_state() = runTest {
        val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
        coEvery { mockDataStore.data} returns flow { throw IOException() }
        val failureRepository = AppSettingsRepositoryImpl(mockDataStore)

        val failureState: AppSettingsState = failureRepository.appSettingsFlow.first()
        val defaultState = AppSettingsState()
        assertThat(failureState.leftHandedMode).isEqualTo(defaultState.leftHandedMode)
        assertThat(failureState.timeFormat).isEqualTo(defaultState.timeFormat)
        assertThat(failureState.dueDateFormat).isEqualTo(defaultState.dueDateFormat)
    }

    @Test(expected = NullPointerException::class)
    fun test_flow_catch_exception() = runTest {
        val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
        coEvery { mockDataStore.data} returns flow { throw NullPointerException() }
        val failureRepository = AppSettingsRepositoryImpl(mockDataStore)

        val failureState: AppSettingsState = failureRepository.appSettingsFlow.first()
    }

    @Test
    fun test_base_state() = runTest {
        val defaultState: AppSettingsState = repository.appSettingsFlow.first()
        assertThat(defaultState)
            .isEqualTo(AppSettingsState())
    }

    @Test
    fun test_setDueDateFormatSetting_base() = runTest {
        val result = repository.appSettingsFlow.first()
        assertThat(result.dueDateFormat)
            .isSameInstanceAs(DueDateFormat.DEFAULT)
    }

    @Test
    fun test_setDueDateFormatSetting_months_success() = runTest {
        with(repository) {
            setDueDateFormatSetting(DueDateFormat.MONTHS)
            val result = appSettingsFlow.first()
            assertThat(result.dueDateFormat)
                .isSameInstanceAs(DueDateFormat.MONTHS)
        }
    }

    @Test
    fun test_setTimeFormatSetting_base() = runTest {
        val result = repository.appSettingsFlow.first()
        assertThat(result.timeFormat)
            .isSameInstanceAs(TimeFormat.DEFAULT)
    }

    @Test
    fun test_setTimeFormatSetting_hr24_success() = runTest {
        with(repository) {
            setTimeFormatSetting(TimeFormat.HR24)
            val result = repository.appSettingsFlow.first()
            assertThat(result.timeFormat)
                .isSameInstanceAs(TimeFormat.HR24)
        }
    }

    @Test
    fun test_setLeftHandedSetting_base() = runTest {
        val result = repository.appSettingsFlow.first()
        assertThat(result.leftHandedMode)
            .isFalse()
    }

    @Test
    fun test_setLeftHandedSetting_true() = runTest {
        with(repository) {
            setLeftHandedSetting(true)
            val result = repository.appSettingsFlow.first()
            assertThat(result.leftHandedMode)
                .isTrue()
        }
    }

    companion object {
        private const val DATA_STORE_NAME: String = "test"
    }
}