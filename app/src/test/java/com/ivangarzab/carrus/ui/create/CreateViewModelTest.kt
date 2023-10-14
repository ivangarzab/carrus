package com.ivangarzab.carrus.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.BLANK_STRING
import com.ivangarzab.carrus.EMPTY_CAR
import com.ivangarzab.carrus.EMPTY_STRING
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.TEST_CAR_JSON
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.getOrAwaitValue
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.util.helpers.TestContentResolverHelper
import io.mockk.every
import io.mockk.justRun
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
    private lateinit var carRepository: CarRepository

    @Before
    fun setup() {
        carRepository = mockk(relaxUnitFun = true)
        viewModel = CreateViewModel(
            carRepository = carRepository,
            contentResolverHelper = TestContentResolverHelper()
        ).apply { init(null) }

        every { carRepository.fetchCarData() } returns TEST_CAR
    }

    @Test
    fun test_init_null_default_create() {
        assertThat(viewModel.type)
            .isSameInstanceAs(CreateViewModel.Type.CREATE)
    }

    @Test
    fun test_init_with_data_edit() = with(viewModel) {
        init(EMPTY_CAR)
        assertThat(type)
            .isSameInstanceAs(CreateViewModel.Type.EDIT)
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
            verifyData(EMPTY_STRING, MODEL, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_model_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            verifyData(MAKE, EMPTY_STRING, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_year_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            verifyData(MAKE, MODEL, EMPTY_STRING)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_onImageUriReceived_success() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onImageUriReceived("uri")
        }
        assertThat(result.imageUri)
            .isNotNull()
    }

    @Test
    fun test_onImageUriReceived_empty_string_failure() = with(viewModel) {
        onImageUriReceived(EMPTY_STRING)
        val result = state.getOrAwaitValue()
        assertThat(result.imageUri)
            .isNull()
    }

    @Test
    fun test_onImageUriReceived_blank_string_failure() = with(viewModel) {
        onImageUriReceived(BLANK_STRING)
        val result = state.getOrAwaitValue()
        assertThat(result.imageUri)
            .isNull()
    }

    @Test
    fun test_onImageDeleted_base() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onImageDeleted()
        }
        assertThat(result.imageUri)
            .isNull()
    }

    @Test
    fun test_onImageDeleted_with_data() = with(viewModel) {
        onImageUriReceived(TEST_URI)
        val result = state.getOrAwaitValue {
            onImageDeleted()
        }
        assertThat(result.imageUri)
            .isNull()
    }

    @Test
    fun test_onUpdateStateData_base() = with(viewModel) {
        state.getOrAwaitValue().let { result ->
            assertThat(result.isExpanded).isFalse()
            assertThat(result.title).isEqualTo(SCREEN_TITLE)
            assertThat(result.actionButton).isEqualTo(ACTION_BUTTON)
            assertThat(result.nickname).isEmpty()
            assertThat(result.make).isEmpty()
            assertThat(result.model).isEmpty()
            assertThat(result.year).isEmpty()
            assertThat(result.licenseNo).isEmpty()
            assertThat(result.vinNo).isEmpty()
            assertThat(result.tirePressure).isEmpty()
            assertThat(result.totalMiles).isEmpty()
            assertThat(result.milesPerGallon).isEmpty()
            assertThat(result.imageUri).isNull()
        }
    }

    @Test
    fun test_onUpdateStateData_updated_data() = with(viewModel) {
        val result = state.getOrAwaitValue {
            onUpdateStateData(
                nickname = NICKNAME,
                make = MAKE,
                model = MODEL,
                year = YEAR,
                licenseNo = LICENSE_NO,
                vinNo = VIN_NO,
                tirePressure = TIRE_PRESSURE,
                totalMiles = TOTAL_MILES,
                milesPerGallon = MILES_PER_GALLON
            )
        }
        assertThat(result.isExpanded).isEqualTo(TEST_CAR_MODAL_STATE.isExpanded)
        assertThat(result.title).isEqualTo(TEST_CAR_MODAL_STATE.title)
        assertThat(result.actionButton).isEqualTo(TEST_CAR_MODAL_STATE.actionButton)
        assertThat(result.nickname).isEqualTo(TEST_CAR_MODAL_STATE.nickname)
        assertThat(result.make).isEqualTo(TEST_CAR_MODAL_STATE.make)
        assertThat(result.model).isEqualTo(TEST_CAR_MODAL_STATE.model)
        assertThat(result.year).isEqualTo(TEST_CAR_MODAL_STATE.year)
        assertThat(result.licenseNo).isEqualTo(TEST_CAR_MODAL_STATE.licenseNo)
        assertThat(result.vinNo).isEqualTo(TEST_CAR_MODAL_STATE.vinNo)
        assertThat(result.tirePressure).isEqualTo(TEST_CAR_MODAL_STATE.tirePressure)
        assertThat(result.totalMiles).isEqualTo(TEST_CAR_MODAL_STATE.totalMiles)
        assertThat(result.milesPerGallon).isEqualTo(TEST_CAR_MODAL_STATE.milesPerGallon)
        assertThat(result.imageUri).isEqualTo(TEST_CAR_MODAL_STATE.imageUri)
    }

    @Test
    fun test_onImportData_empty_string_failure() {
        assertThat(viewModel.onImportData(EMPTY_STRING))
            .isFalse()
    }

    @Test
    fun test_onImportData_blank_string_failure() {
        assertThat(viewModel.onImportData(BLANK_STRING))
            .isFalse()
    }

    @Test
    fun test_onImportData_invalid_json_failure() {
        assertThat(viewModel.onImportData(TEST_URI))
            .isFalse()
    }

    @Test
    fun test_onImportData_valid_json_success() = with(viewModel) {
        justRun { carRepository.saveCarData(any()) }
        assertThat(onImportData(TEST_CAR_JSON))
            .isTrue()
    }

    companion object {
        private const val TEST_URI = "uri"
        private const val NICKNAME = "Shaq"
        private const val MODEL = "Malibu"
        private const val MAKE = "Chevrolet"
        private const val YEAR = "2008"
        private const val LICENSE_NO = "09TX956"
        private const val VIN_NO = "ABCDEFGHIJKLMNOPQ"
        private const val TIRE_PRESSURE = "32"
        private const val TOTAL_MILES = "106000"
        private const val MILES_PER_GALLON = "23"
        private const val SCREEN_TITLE = "Add a Car"
        private const val ACTION_BUTTON = "Submit"
        private val TEST_CAR_MODAL_STATE = CarModalState(
            isExpanded = false,
            title = SCREEN_TITLE,
            actionButton = ACTION_BUTTON,
            nickname = NICKNAME,
            make = MAKE,
            model = MODEL,
            year = YEAR,
            licenseNo = LICENSE_NO,
            vinNo = VIN_NO,
            tirePressure = TIRE_PRESSURE,
            totalMiles = TOTAL_MILES,
            milesPerGallon = MILES_PER_GALLON,
            imageUri = null
        )

    }
}