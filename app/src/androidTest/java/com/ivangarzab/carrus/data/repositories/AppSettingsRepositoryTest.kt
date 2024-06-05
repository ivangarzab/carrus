package com.ivangarzab.carrus.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.states.AppSettingsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsRepositoryTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val testCoroutineDispatcher: TestCoroutineDispatcher =
        TestCoroutineDispatcher()
    private val testCoroutineScope =
        createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + (testCoroutineDispatcher + Job()))

    private lateinit var testDataStore: DataStore<Preferences>

    private lateinit var repository: AppSettingsRepositoryImpl

    @Before
    fun setup() {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile = { context.preferencesDataStoreFile(DATA_STORE_NAME) }
        )
        repository = AppSettingsRepositoryImpl(testDataStore)
    }

    @After
    fun teardown() {
        context.preferencesDataStoreFile(DATA_STORE_NAME).deleteRecursively()
        testCoroutineScope.cancel()
    }

    @Test
    fun test_base_state() = testCoroutineScope.runTest {
        val defaultState: AppSettingsState = repository.appSettingsFlow.first()
        assertThat(defaultState)
            .isEqualTo(AppSettingsState())
    }

    @Test
    fun test_setDueDateFormatSetting_base() = testCoroutineScope.runTest {
        val result: DueDateFormat = repository.appSettingsFlow.first().dueDateFormat
        assertThat(result)
            .isEqualTo(DueDateFormat.DEFAULT)
    }

    @Test
    fun test_setDueDateFormatSetting_months_success() = testCoroutineScope.runTest {
        with(repository) {
            setDueDateFormatSetting(DueDateFormat.MONTHS)
            val result: DueDateFormat = appSettingsFlow.first().dueDateFormat
            assertThat(result).isEqualTo(DueDateFormat.MONTHS)
            assertThat(result).isNotEqualTo(DueDateFormat.WEEKS)
        }
    }

    @Test
    fun test_setTimeFormatSetting_base() = testCoroutineScope.runTest {
        val result: TimeFormat = repository.appSettingsFlow.first().timeFormat
        assertThat(result)
            .isEqualTo(TimeFormat.DEFAULT)
    }

    @Test
    fun test_setTimeFormatSetting_hr24_success() = testCoroutineScope.runTest {
        with(repository) {
            setTimeFormatSetting(TimeFormat.HR24)
            val result: TimeFormat = appSettingsFlow.first().timeFormat
            assertThat(result)
                .isEqualTo(TimeFormat.HR24)
        }
    }

    @Test
    fun test_setLeftHandedSetting_base() = testCoroutineScope.runTest {
        val result: Boolean = repository.appSettingsFlow.first().leftHandedMode
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_setLeftHandedSetting_true() = testCoroutineScope.runTest {
        with(repository) {
            setLeftHandedSetting(true)
            val result: Boolean = appSettingsFlow.first().leftHandedMode
            assertThat(result)
                .isTrue()
        }
    }

    companion object {
        private const val DATA_STORE_NAME: String = "test"
    }
}