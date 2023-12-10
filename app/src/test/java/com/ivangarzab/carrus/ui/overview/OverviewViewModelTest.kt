package com.ivangarzab.carrus.ui.overview

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.MainDispatcherRule
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.TEST_SERVICE
import com.ivangarzab.carrus.data.models.Message
import com.ivangarzab.carrus.data.repositories.MessageQueueRepository
import com.ivangarzab.carrus.data.repositories.TestAlarmsRepository
import com.ivangarzab.carrus.data.repositories.TestAppSettingsRepository
import com.ivangarzab.carrus.data.repositories.TestCarRepository
import com.ivangarzab.carrus.getOrAwaitValue
import com.ivangarzab.carrus.ui.overview.data.SortingType
import com.ivangarzab.carrus.util.providers.BuildVersionProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: OverviewViewModel

    private lateinit var carRepository: TestCarRepository
    private lateinit var appSettingsRepository: TestAppSettingsRepository
    private lateinit var alarmsRepository: TestAlarmsRepository
    private lateinit var messageQueueRepository: MessageQueueRepository
    private lateinit var buildVersionProvider: BuildVersionProvider

    @Before
    fun setup() {
        carRepository = TestCarRepository()
        appSettingsRepository = TestAppSettingsRepository()
        alarmsRepository = TestAlarmsRepository()
        messageQueueRepository = MessageQueueRepository()
        buildVersionProvider = mockk(relaxed = true)

        viewModel = OverviewViewModel(
            buildVersionProvider = buildVersionProvider,
            carRepository = carRepository,
            appSettingsRepository = appSettingsRepository,
            alarmsRepository = alarmsRepository,
            messageQueueRepository = messageQueueRepository,
            analytics = mockk(relaxUnitFun = true)
        )
    }

    @Test
    fun test_onServiceDeleted_success() = with(viewModel) {
        carRepository.let {
            it.saveCarData(TEST_CAR)
            it.addCarService(TEST_SERVICE)
        }
        state.getOrAwaitValue().let {
            assertThat(it.car).isNotNull()
            assertThat(it.car?.services?.contains(TEST_SERVICE)).isTrue()
        }
        val result = state.getOrAwaitValue {
            onServiceDeleted(TEST_SERVICE)
        }
        assertThat(result.car?.services?.contains(TEST_SERVICE))
            .isFalse()
    }

    @Test
    fun test_onServiceDeleted_service_not_there() = with(viewModel) {
        carRepository.saveCarData(TEST_CAR)
        state.getOrAwaitValue().let {
            assertThat(it.car).isNotNull()
            assertThat(it.car?.services?.contains(TEST_SERVICE)).isFalse()
        }
        val result = state.getOrAwaitValue {
            onServiceDeleted(TEST_SERVICE)
        }
        assertThat(result.car?.services?.contains(TEST_SERVICE))
            .isFalse()
    }

    @Test
    fun test_onNotificationPermissionActivityResult_false() = with(viewModel) {
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onNotificationPermissionActivityResult(false)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_onNotificationPermissionActivityResult_false_with_message() = with(viewModel) {
        addNotificationPermissionMessage()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onNotificationPermissionActivityResult(false)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_onNotificationPermissionActivityResult_true() = with(viewModel) {
        addNotificationPermissionMessage()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onNotificationPermissionActivityResult(true)
        }
        assertThat(result)
            .isNotEqualTo(expected)
    }

    @Test
    fun test_onMessageClicked_missingPermissionNotification() = with(viewModel) {
        addNotificationPermissionMessage()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onMessageClicked(Message.MISSING_PERMISSION_NOTIFICATION.data.id)
        }
        assertThat(result)
            .isNotEqualTo(expected)
    }

    @Test
    fun test_onMessageClicked_missingPermissionAlarm() = with(viewModel) {
        addAlarmPermissionMessage()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onMessageClicked(Message.MISSING_PERMISSION_ALARM.data.id)
        }
        assertThat(result)
            .isNotEqualTo(expected)
    }

    @Test
    fun test_onMessageClicked_invalid_id_failure() = with(viewModel) {
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onMessageClicked(INVALID_ID)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test//(expected = NoSuchElementException::class)
    fun test_onMessageDismissed_from_empty_list_failure() = with(viewModel) {
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onMessageDismissed() // this will throw on an empty list
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_onMessageDismissed_success() = with(viewModel) {
        addAlarmPermissionMessage()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            onMessageDismissed()
        }
        assertThat(result)
            .isNotEqualTo(expected)
    }

    @Test
    fun test_checkForAlarmPermissions_canScheduleExactAlarms_true() = with(viewModel) {
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            checkForAlarmPermission(true)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_checkForAlarmPermissions_canScheduleExactAlarms_false_hasPromptedForPermissionAlarm_true() = with(viewModel) {
        addAlarmPermissionMessage()
        onMessageDismissed()
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            checkForAlarmPermission(false)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_checkForAlarmPermissions_canScheduleExactAlarms_false_hasPromptedForPermissionAlarm_false_incorrect_sdk() = with(viewModel) {
        every { buildVersionProvider.getSdkVersionInt() } returns Build.VERSION_CODES.R
        val expected = queueState.value
        val result = queueState.getOrAwaitValue {
            checkForAlarmPermission(false)
        }
        assertThat(result)
            .isEqualTo(expected)
    }

    @Test
    fun test_checkForAlarmPermissions_canScheduleExactAlarms_false_hasPromptedForPermissionAlarm_false_correct_sdk() {
        every { buildVersionProvider.getSdkVersionInt() } returns Build.VERSION_CODES.S
        with(viewModel) {
            val expected = queueState.value
            val result = queueState.getOrAwaitValue {
                checkForAlarmPermission(false)
            }
            assertThat(result)
                .isNotEqualTo(expected)
        }
    }

    @Test
    fun test_onSort_base() = with(viewModel) {
        val result = state.getOrAwaitValue()
        assertThat(result.serviceSortingType)
            .isSameInstanceAs(SortingType.NONE)
    }

    @Test
    fun test_onSort_none_state_update() = with(viewModel) {
        val test = SortingType.NONE
        val result = state.getOrAwaitValue {
            onSort(test)
        }
        assertThat(result.serviceSortingType)
            .isSameInstanceAs(test)
    }

    @Test
    fun test_onSort_name_state_update() = with(viewModel) {
        val test = SortingType.NAME
        val result = state.getOrAwaitValue {
            onSort(test)
        }
        assertThat(result.serviceSortingType)
            .isSameInstanceAs(test)
    }

    @Test
    fun test_onSort_date_state_update() = with(viewModel) {
        val test = SortingType.DUE_DATE
        val result = state.getOrAwaitValue {
            onSort(test)
        }
        assertThat(result.serviceSortingType)
            .isSameInstanceAs(test)
    }

    companion object {
        private const val INVALID_ID = "invalid-id"
    }
}