package com.ivangarzab.carrus.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.data.repositories.CarRepository
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

    private var onVerify: Boolean = false
    private val onVerifyObserver = Observer<Boolean> {
        onVerify = it
    }

    @Before
    fun setup() {
        val carRepository: CarRepository = mockk(relaxUnitFun = true)
        viewModel = CreateViewModel(
            SavedStateHandle(),
            carRepository = carRepository
        ).apply {
            init(null)
            onVerify.observeForever(onVerifyObserver)
        }

        every { carRepository.fetchCarData() } returns TEST_CAR
    }

    @Test
    fun test_verifyData_true() {
        viewModel.verifyData(MAKE, MODEL, YEAR)
        TestCase.assertEquals(true, onVerify)
    }

    @Test
    fun test_verifyData_bogus_make_false() {
        viewModel.verifyData(EMPTY, MODEL, YEAR)
        TestCase.assertEquals(false, onVerify)
    }

    @Test
    fun test_verifyData_bogus_model_false() {
        viewModel.verifyData(MAKE, EMPTY, YEAR)
        TestCase.assertEquals(false, onVerify)
    }

    @Test
    fun test_verifyData_bogus_year_false() {
        viewModel.verifyData(MAKE, MODEL, EMPTY)
        TestCase.assertEquals(false, onVerify)
    }

    companion object {
        private const val EMPTY = ""
        private const val MAKE = "make"
        private const val MODEL = "model"
        private const val YEAR = "year"
    }
}