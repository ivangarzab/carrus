package com.ivangarzab.carrus.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateViewModel

    @Before
    fun setup() {
        val carRepository: CarRepository = mockk(relaxUnitFun = true)
        viewModel = CreateViewModel(
            carRepository = carRepository
        ).apply { init(null) }

        every { carRepository.fetchCarData() } returns TEST_CAR
    }

    @Test
    fun test_verifyData_true() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            verifyData(MAKE, MODEL, YEAR)
        }
        TestCase.assertEquals(true, result)
    }

    @Test
    fun test_verifyData_bogus_make_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            viewModel.verifyData(EMPTY, MODEL, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_model_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            viewModel.verifyData(MAKE, EMPTY, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_year_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            viewModel.verifyData(MAKE, MODEL, EMPTY)
        }
        TestCase.assertEquals(false, result)
    }

    companion object {
        private const val EMPTY = ""
        private const val MAKE = "make"
        private const val MODEL = "model"
        private const val YEAR = "year"
    }
}