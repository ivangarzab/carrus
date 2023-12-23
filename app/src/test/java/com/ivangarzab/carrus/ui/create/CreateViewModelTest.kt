package com.ivangarzab.carrus.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.di.DebugFlagProviderImpl
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.TestCarRepository
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.util.helpers.TestContentResolverHelper
import com.ivangarzab.carrus.util.managers.CarImporter
import com.ivangarzab.test_data.CAR_EMPTY
import com.ivangarzab.test_data.STRING_BLANK
import com.ivangarzab.test_data.STRING_EMPTY
import com.ivangarzab.test_data.STRING_TEST
import com.ivangarzab.test_data.getOrAwaitValue
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
        carRepository = TestCarRepository()
        viewModel = CreateViewModel(
            carRepository = carRepository,
            contentResolverHelper = TestContentResolverHelper(),
            analytics = mockk(relaxUnitFun = true),
            DebugFlagProviderImpl().apply { forceDebug = true },
            CarImporter()
        ).apply { init(null) }
    }

    @Test
    fun test_init_null_default_create() {
        assertThat(viewModel.type)
            .isSameInstanceAs(CreateViewModel.Type.CREATE)
    }

    @Test
    fun test_init_with_data_edit() = with(viewModel) {
        init(CAR_EMPTY)
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
            verifyData(STRING_EMPTY, MODEL, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_model_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            verifyData(MAKE, STRING_EMPTY, YEAR)
        }
        TestCase.assertEquals(false, result)
    }

    @Test
    fun test_verifyData_bogus_year_false() = with(viewModel) {
        val result = onVerify.getOrAwaitValue {
            verifyData(MAKE, MODEL, STRING_EMPTY)
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
        onImageUriReceived(STRING_EMPTY)
        val result = state.getOrAwaitValue()
        assertThat(result.imageUri)
            .isNull()
    }

    @Test
    fun test_onImageUriReceived_blank_string_failure() = with(viewModel) {
        onImageUriReceived(STRING_BLANK)
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
        onImageUriReceived(STRING_TEST)
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
            assertThat(result.licenseState).isEmpty()
            assertThat(result.licenseNo).isEmpty()
            assertThat(result.vinNo).isEmpty()
            assertThat(result.tirePressure).isEmpty()
            assertThat(result.totalMiles).isEmpty()
            assertThat(result.milesPerGalCity).isEmpty()
            assertThat(result.milesPerGalHighway).isEmpty()
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
                licenseState = LICENSE_STATE,
                licenseNo = LICENSE_NO,
                vinNo = VIN_NO,
                tirePressure = TIRE_PRESSURE,
                totalMiles = TOTAL_MILES,
                milesPerGalCity = MILES_PER_GALLON_CITY,
                milesPerGalHighway = MILES_PER_GALLON_HIGHWAY,
            )
        }
        assertThat(result.isExpanded).isEqualTo(TEST_CAR_MODAL_STATE.isExpanded)
        assertThat(result.title).isEqualTo(TEST_CAR_MODAL_STATE.title)
        assertThat(result.actionButton).isEqualTo(TEST_CAR_MODAL_STATE.actionButton)
        assertThat(result.nickname).isEqualTo(TEST_CAR_MODAL_STATE.nickname)
        assertThat(result.make).isEqualTo(TEST_CAR_MODAL_STATE.make)
        assertThat(result.model).isEqualTo(TEST_CAR_MODAL_STATE.model)
        assertThat(result.year).isEqualTo(TEST_CAR_MODAL_STATE.year)
        assertThat(result.licenseState).isEqualTo(TEST_CAR_MODAL_STATE.licenseState)
        assertThat(result.licenseNo).isEqualTo(TEST_CAR_MODAL_STATE.licenseNo)
        assertThat(result.vinNo).isEqualTo(TEST_CAR_MODAL_STATE.vinNo)
        assertThat(result.tirePressure).isEqualTo(TEST_CAR_MODAL_STATE.tirePressure)
        assertThat(result.totalMiles).isEqualTo(TEST_CAR_MODAL_STATE.totalMiles)
        assertThat(result.milesPerGalCity).isEqualTo(TEST_CAR_MODAL_STATE.milesPerGalCity)
        assertThat(result.milesPerGalHighway).isEqualTo(TEST_CAR_MODAL_STATE.milesPerGalHighway)
        assertThat(result.imageUri).isEqualTo(TEST_CAR_MODAL_STATE.imageUri)
    }

    /*@Test(expected = NullPointerException::class)
    fun test_onImportData_null_uri_exception_failure() {
        assertThat(viewModel.onImportData(URI_EMPTY))
            .isFalse()
    }

    @Test
    fun test_onImportData_invalid_json_failure() {
        assertThat(viewModel.onImportData(URI_TEST))
            .isFalse()
    }

    @Test
    fun test_onImportData_valid_json_success() = with(viewModel) {
        assertThat(onImportData(mockk()))
            .isTrue()
    }*/

    companion object {
        private const val NICKNAME = "Shaq"
        private const val MODEL = "Malibu"
        private const val MAKE = "Chevrolet"
        private const val YEAR = "2008"
        private const val LICENSE_STATE = "Texas"
        private const val LICENSE_NO = "09TX956"
        private const val VIN_NO = "ABCDEFGHIJKLMNOPQ"
        private const val TIRE_PRESSURE = "32"
        private const val TOTAL_MILES = "106000"
        private const val MILES_PER_GALLON_CITY = "23"
        private const val MILES_PER_GALLON_HIGHWAY = "28"
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
            licenseState = LICENSE_STATE,
            licenseNo = LICENSE_NO,
            vinNo = VIN_NO,
            tirePressure = TIRE_PRESSURE,
            totalMiles = TOTAL_MILES,
            milesPerGalCity = MILES_PER_GALLON_CITY,
            milesPerGalHighway = MILES_PER_GALLON_HIGHWAY,
            imageUri = null
        )

    }
}