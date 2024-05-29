package com.ivangarzab.carrus.ui.modal_service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.test_data.SERVICE_EMPTY
import com.ivangarzab.test_data.SERVICE_TEST_1
import com.ivangarzab.test_data.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceModalViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ServiceModalViewModel

    private val mockAnalytics: Analytics = mockk(relaxUnitFun = true)

    private val mockCarRepository: CarRepository = mockk(relaxUnitFun = true)

    private var state: ServiceModalState? = null
    private val stateObserver = Observer<ServiceModalState?> {
        state = it
    }

    private var onSubmission: Boolean? = null
    private val onSubmissionObserver = Observer<Boolean> {
        onSubmission = it
    }

    @Before
    fun setup() {
        viewModel = ServiceModalViewModel(
            carRepository = mockCarRepository,
            analytics = mockAnalytics
        )
        every { mockCarRepository.fetchCarData() } returns Car.default
        every { mockCarRepository.updateCarService(SERVICE_TEST_1) }

        viewModel.state.observeForever(stateObserver)
        viewModel.onSubmission.observeForever(onSubmissionObserver)
    }

    @After
    fun teardown() {
        viewModel.state.removeObserver(stateObserver)
        viewModel.onSubmission.removeObserver(onSubmissionObserver)
    }

    @Test
    fun test_setInitialData_base() {
        setEmptyInitialData()
        assertThat(viewModel.modalMode)
            .isSameInstanceAs(ServiceModalState.Mode.NULL)
    }

    @Test
    fun test_setInitialData_with_edit_data() {
        setEditInitialData()
        assertThat(viewModel.modalMode)
            .isSameInstanceAs(ServiceModalState.Mode.EDIT)
    }

    @Test
    fun test_setInitialData_with_reschedule_data() {
        setRescheduleInitialData()
        assertThat(viewModel.modalMode)
            .isSameInstanceAs(ServiceModalState.Mode.RESCHEDULE)
        viewModel.isShowingRepairDateDialog.value?.let { isShowingRepairDateDialog ->
            assertThat(isShowingRepairDateDialog).isTrue()
        }
    }

    @Test
    fun test_onUpdateServiceModalState_empty_data() {
        with(SERVICE_MODAL_STATE_INVALID) {
            setEmptyInitialData()
            viewModel.onUpdateServiceModalState(this)
            state?.let {
                assertThat(it.name).matches(name)
                assertThat(it.repairDate).matches(repairDate)
                assertThat(it.dueDate).matches(dueDate)
            }
        }
    }

    @Test
    fun test_onUpdateServiceModalState_valid_data() {
        with(SERVICE_MODAL_STATE_VALID) {
            setEmptyInitialData()
            viewModel.onUpdateServiceModalState(this)
            state?.let {
                assertThat(it.name).matches(name)
                assertThat(it.repairDate).matches(repairDate)
                assertThat(it.dueDate).matches(dueDate)
            }
        }
    }

    @Test
    fun test_onActionButtonClicked_without_state_data_onSubmission_return_false() {
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isFalse()
    }

    @Test
    fun test_onActionButtonClicked_with_empty_data_onSubmission_return_false() {
        setEmptyInitialData()
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isFalse()
    }

    @Test
    fun test_onActionButtonClicked_with_state_create_data_onSubmission_return_true() {
        setCreateInitialData()
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isTrue()
    }

    @Test
    fun test_onActionButtonClicked_with_state_edit_data_onSubmission_return_true() {
        setEditInitialData()
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isTrue()
    }

    @Test
    fun test_onActionButtonClicked_with_state_reschedule_data_onSubmission_return_true() {
        setRescheduleInitialData()
        viewModel.apply {
            state.value?.let { state ->
                onUpdateServiceModalState(state.copy(dueDate = "7/31/24"))
            }
            onActionButtonClicked()
        }
        assertThat(onSubmission)
            .isTrue()
    }

    @Test
    fun test_verifyStringDate_blank_data() {
        val result = viewModel.verifyStringDate("")
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_onShowRepairDateDialog() {
        val result = viewModel.isShowingRepairDateDialog.getOrAwaitValue {
            viewModel.onShowRepairDateDialog()
        }
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_onHideRepairDateDialog() {
        viewModel.onShowRepairDateDialog()
        val result1 = viewModel.isShowingRepairDateDialog.getOrAwaitValue {
            viewModel.onHideRepairDateDialog()
        }
        val result2 = viewModel.isShowingDueDateDialog.getOrAwaitValue()
        assertThat(result1).isFalse()
        assertThat(result2).isTrue()
    }

    @Test
    fun test_onShowDueDateDialog() {
        val result = viewModel.isShowingDueDateDialog.getOrAwaitValue {
            viewModel.onShowDueDateDialog()
        }
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_oonHideDueDateDialog() {
            viewModel.onShowDueDateDialog()
        val result = viewModel.isShowingDueDateDialog.getOrAwaitValue {
            viewModel.onHideDueDateDialog()
        }
        assertThat(result)
            .isFalse()
    }

    private fun setEmptyInitialData() = viewModel.setInitialData(SERVICE_EMPTY, ServiceModalState.Mode.NULL)

    private fun setCreateInitialData() = viewModel.setInitialData(SERVICE_TEST_1, ServiceModalState.Mode.CREATE)

    private fun setEditInitialData() = viewModel.setInitialData(SERVICE_TEST_1, ServiceModalState.Mode.EDIT)

    private fun setRescheduleInitialData() = viewModel.setInitialData(SERVICE_TEST_1, ServiceModalState.Mode.RESCHEDULE)

    companion object {
        private val SERVICE_MODAL_STATE_INVALID = ServiceModalState(
            name = "",
            repairDate = "",
            dueDate = "",
        )

        private val SERVICE_MODAL_STATE_VALID = ServiceModalState(
            name = "Valid Service",
            repairDate = "7/31/23",
            dueDate = "7/31/23",
            price = "9.99"
        )
    }
}