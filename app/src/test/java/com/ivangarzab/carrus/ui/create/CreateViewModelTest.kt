package com.ivangarzab.carrus.ui.create

import androidx.lifecycle.SavedStateHandle
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.repositories.CarRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModelTest {

    private lateinit var viewModel: CreateViewModel

    @Before
    fun setup() {
        val carRepository: CarRepository = mockk()
        viewModel = CreateViewModel(
            SavedStateHandle(),
            carRepository = carRepository
        )

        every { carRepository.fetchCarData() } returns Car.empty
    }

    @Test
    fun test_verifyData_true() {
        TestCase.assertEquals(true, viewModel.verifyData(MAKE, MODEL, YEAR))
    }

    @Test
    fun test_verifyData_bogus_make_false() {
        TestCase.assertEquals(false, viewModel.verifyData(EMPTY, MODEL, YEAR))
    }

    @Test
    fun test_verifyData_bogus_model_false() {
        TestCase.assertEquals(false, viewModel.verifyData(MAKE, EMPTY, YEAR))
    }

    @Test
    fun test_verifyData_bogus_year_false() {
        TestCase.assertEquals(false, viewModel.verifyData(MAKE, MODEL, EMPTY))
    }

    companion object {
        private const val EMPTY = ""
        private const val MAKE = "make"
        private const val MODEL = "model"
        private const val YEAR = "year"
    }
}