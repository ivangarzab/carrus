package com.ivangarzab.carrus.ui.modals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.test_data.SERVICE_EMPTY
import com.ivangarzab.test_data.SERVICE_TEST_1
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
        val carRepository: CarRepository = mockk(relaxed = true)
        viewModel = ServiceModalViewModel(
            carRepository = carRepository,
            analytics = mockk(relaxUnitFun = true)
        )
        every { carRepository.fetchCarData() } returns Car.default
        every { carRepository.updateCarService(SERVICE_TEST_1) }

        viewModel.state.observeForever(stateObserver)
        viewModel.onSubmission.observeForever(onSubmissionObserver)
    }

    @After
    fun teardown() {
        viewModel.state.removeObserver(stateObserver)
        viewModel.onSubmission.removeObserver(onSubmissionObserver)
    }

    @Test
    fun test_setArgsData_base() {
        viewModel.setArgsData(null)
        assertThat(viewModel.modalType)
            .isSameInstanceAs(ServiceModalViewModel.Type.CREATE)
    }

    @Test
    fun test_setArgsData_with_data() {
        setArgsData()
        assertThat(viewModel.modalType)
            .isSameInstanceAs(ServiceModalViewModel.Type.EDIT)
    }

    @Test
    fun test_onUpdateServiceModalState_empty_data() {
        with(SERVICE_MODAL_STATE_INVALID) {
            setEmptyArgsData()
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
            setEmptyArgsData()
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
        setEmptyArgsData()
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isFalse()
    }

    @Test
    fun test_onActionButtonClicked_with_state_data_onSubmission_return_true() {
        setArgsData()
        viewModel.onActionButtonClicked()
        assertThat(onSubmission)
            .isTrue()
    }

    private fun setEmptyArgsData() = viewModel.setArgsData(SERVICE_EMPTY)
    private fun setArgsData() = viewModel.setArgsData(SERVICE_TEST_1)

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