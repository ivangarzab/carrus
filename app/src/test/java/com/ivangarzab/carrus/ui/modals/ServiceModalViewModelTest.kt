package com.ivangarzab.carrus.ui.modals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.TEST_SERVICE
import com.ivangarzab.carrus.TEST_SERVICE_EMPTY
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
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

    private var state: ServiceModalViewModel.ServiceModalState? = null
    private val stateObserver = Observer<ServiceModalViewModel.ServiceModalState?> {
        state = it
    }

    private var onDataRequest: Pair<Boolean, ServiceModalViewModel.DataRequest>? = null
    private val onDataRequestObserver = Observer<Pair<Boolean, ServiceModalViewModel.DataRequest>> {
        onDataRequest = it
    }

    private var onSubmission: Boolean? = null
    private val onSubmissionObserver = Observer<Boolean> {
        onSubmission = it
    }

    @Before
    fun setup() {
        val carRepository: CarRepository = mockk(relaxed = true)
        viewModel = ServiceModalViewModel(
            carRepository = carRepository
        )
        every { carRepository.fetchCarData() } returns Car.default
        every { carRepository.updateCarService(TEST_SERVICE) }

        viewModel.state.observeForever(stateObserver)
        viewModel.onDataRequest.observeForever(onDataRequestObserver)
        viewModel.onSubmission.observeForever(onSubmissionObserver)
    }

    @After
    fun teardown() {
        viewModel.state.removeObserver(stateObserver)
        viewModel.onDataRequest.removeObserver(onDataRequestObserver)
        viewModel.onSubmission.removeObserver(onSubmissionObserver)
    }

    @Test
    fun test_setArgsData_type_with_null() {
        viewModel.setArgsData(null)
        assertThat(state?.type)
            .isSameInstanceAs(ServiceModalViewModel.Type.CREATE)
    }

    @Test
    fun test_setArgsData_data_with_null() {
        viewModel.setArgsData(null)
        assertThat(state?.data)
            .isNull()
    }

    @Test
    fun test_setArgsData_type_with_data() {
        setArgsData()
        assertThat(state?.type)
            .isSameInstanceAs(ServiceModalViewModel.Type.EDIT)
    }

    @Test
    fun test_setArgsData_data_with_data() {
        setArgsData()
        assertThat(state?.data)
            .isEqualTo(TEST_SERVICE_EMPTY)
    }

    @Test
    fun test_onActionButtonClicked_without_state_data_first() {
        viewModel.onActionButtonClicked()
        assertThat(onDataRequest?.first)
            .isFalse()
    }

    @Test
    fun test_onActionButtonClicked_without_state_data_second() {
        viewModel.onActionButtonClicked()
        assertThat(onDataRequest?.second)
            .isEqualTo(ServiceModalViewModel.DataRequest())
    }

    @Test
    fun test_onActionButtonClicked_with_state_data_first() {
        setArgsData()
        viewModel.onActionButtonClicked()
        assertThat(onDataRequest?.first)
            .isTrue()
    }

    @Test
    fun test_onActionButtonClicked_with_state_data_second() {
        setArgsData()
        viewModel.onActionButtonClicked()
        assertThat(onDataRequest?.second)
            .isEqualTo(ServiceModalViewModel.DataRequest(
                type = state?.type ?: ServiceModalViewModel.Type.EDIT,
                id = TEST_SERVICE_EMPTY.id
            ))
    }

    @Test
    fun test_onSubmitData_with_bogus_data_fail() {
        viewModel.onSubmitData(TEST_SERVICE_EMPTY)
        assertThat(onSubmission)
            .isFalse()
    }

    @Test
    fun test_onSubmitData_with_data_without_state_data() {
        viewModel.onSubmitData(TEST_SERVICE)
        assertThat(onSubmission)
            .isTrue()
    }

    @Test
    fun test_onSubmitData_with_data_with_state_data() {
        viewModel.setArgsData(TEST_SERVICE)
        viewModel.onSubmitData(TEST_SERVICE.copy(
            cost = 99.99f
        ))
        assertThat(onSubmission)
            .isTrue()
    }

    @Test
    fun test_getRepairDateInMillis_without_data() {
        assertThat(viewModel.getRepairDateInMillis())
            .isEqualTo(0L)
    }

    @Test
    fun test_getRepairDateInMillis_with_data() = with(viewModel) {
        setArgsData(TEST_SERVICE)
        assertThat(getRepairDateInMillis())
            .isEqualTo(TEST_SERVICE.repairDate.timeInMillis)
    }

    @Test
    fun test_getDueDateInMillis_without_data() {
        assertThat(viewModel.getDueDateInMillis())
            .isEqualTo(0L)
    }

    @Test
    fun test_getDueDateInMillis_with_data() = with(viewModel) {
        setArgsData(TEST_SERVICE)
        assertThat(getDueDateInMillis())
            .isEqualTo(TEST_SERVICE.dueDate.timeInMillis)
    }

    @Test
    fun test_setNewRepairDateInMillis_without_state_data() = with(viewModel) {
        setNewRepairDateInMillis(1639120980000)
        assertThat(getRepairDateInMillis())
            .isEqualTo(0L)
    }

    @Test
    fun test_setNewRepairDateInMillis_with_state_data() = with(viewModel) {
        setArgsData(TEST_SERVICE)
        setNewRepairDateInMillis(1639120980000)
        assertThat(getRepairDateInMillis())
            .isEqualTo(1639120980000)
    }

    @Test
    fun test_setDueDateInMillis_without_state_data() = with(viewModel) {
        setNewDueDateInMillis(1639120980000)
        assertThat(getDueDateInMillis())
            .isEqualTo(0L)
    }

    @Test
    fun test_setNewDueDateInMillis_with_state_data() = with(viewModel) {
        setArgsData(TEST_SERVICE)
        setNewDueDateInMillis(1639120980000)
        assertThat(getDueDateInMillis())
            .isEqualTo(1639120980000)
    }

    private fun setArgsData() {
        viewModel.setArgsData(TEST_SERVICE_EMPTY)
    }
}