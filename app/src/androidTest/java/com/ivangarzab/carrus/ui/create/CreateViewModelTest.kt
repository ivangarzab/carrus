package com.ivangarzab.carrus.ui.create

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateViewModelTest {

    private val viewModel: CreateViewModel = CreateViewModel(
        savedState = SavedStateHandle()
    )

    @Test
    fun test_verifyData_true() {
        assertEquals(true, viewModel.verifyData(MAKE, MODEL, YEAR))
    }

    @Test
    fun test_verifyData_bogus_make_false() {
        assertEquals(false, viewModel.verifyData(EMPTY, MODEL, YEAR))
    }

    @Test
    fun test_verifyData_bogus_model_false() {
        assertEquals(false, viewModel.verifyData(MAKE, EMPTY, YEAR))
    }

    @Test
    fun test_verifyData_bogus_year_false() {
        assertEquals(false, viewModel.verifyData(MAKE, MODEL, EMPTY))
    }

    companion object {
        private const val EMPTY = ""
        private const val MAKE = "make"
        private const val MODEL = "model"
        private const val YEAR = "year"
    }
}