package com.ivangarzab.carrus.util.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * The purpose of this file is to test the [NightThemeManager] class.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NightThemeManagerTest {

    val mockContext: Context = mockk(relaxed = true)

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFolder.newFile("${DATA_STORE_NAME}.preferences_pb") }
    )

    private lateinit var nightThemeManager: NightThemeManager

    @Before
    fun setup() {
        nightThemeManager = NightThemeManager(
            dataStore = testDataStore,
            context = mockContext,
            coroutineScope = testScope
        )
    }

    @Test
    fun test_fetchNightThemeSetting_true() = runTest {
        nightThemeManager.setNightThemeSetting(true)
        val result = nightThemeManager.fetchNightThemeSetting()
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_fetchNightThemeSetting_false() = runTest {
        nightThemeManager.setNightThemeSetting(false)
        val result = nightThemeManager.fetchNightThemeSetting()
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_fetchNightThemeSetting_null() = runTest {
        val mockDataStore: DataStore<Preferences> = getMockDataStore()
        coEvery { mockDataStore.data.firstOrNull() } returns null

        nightThemeManager.fetchNightThemeSetting()
    }

    private fun getMockDataStore(): DataStore<Preferences> {
        val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
        nightThemeManager = NightThemeManager(
            dataStore = mockDataStore,
            context = mockContext,
            coroutineScope = testScope
        )
        return mockDataStore
    }

    companion object {
        private const val DATA_STORE_NAME: String = "test"
    }
}