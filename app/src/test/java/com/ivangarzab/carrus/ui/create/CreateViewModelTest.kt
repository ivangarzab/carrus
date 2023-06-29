package com.ivangarzab.carrus.ui.create

import androidx.lifecycle.SavedStateHandle
import com.ivangarzab.carrus.data.repositories.CarRepository
import junit.framework.TestCase
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CreateViewModelTest {

    private val viewModel = CreateViewModel(
        SavedStateHandle(),
        carRepository = CarRepository()
    )

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