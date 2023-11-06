package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.STRING_BLANK
import com.ivangarzab.carrus.STRING_EMPTY
import com.ivangarzab.carrus.TEST_CAR_JSON
import com.ivangarzab.carrus.data.models.Car
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarValidatorTest {

    @Test
    fun test_validateCarData_null_input_return_empty_car() {
        val result = CarValidator.validateCarData(null)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    @Test
    fun test_validateCarData_empty_string_input_return_empty_car() {
        val result = CarValidator.validateCarData(STRING_EMPTY)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    @Test
    fun test_validateCarData_blank_string_input_return_empty_car() {
        val result = CarValidator.validateCarData(STRING_BLANK)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    @Test
    fun test_validateCarData_empty_json_input_return_empty_car() {
        val result = CarValidator.validateCarData(EMPTY_JSON)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    @Test
    fun test_validateCarData_invalid_json_input_return_empty_car() {
        val result = CarValidator.validateCarData(INVALID_JSON)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    @Test
    fun test_validateCarData_valid_json_with_no_version_input_return_SOMETHING() {
        val result = CarValidator.validateCarData(TEST_CAR_JSON)
        assertThat(result)
            .isEqualTo(Car.empty)
    }

    companion object {
        private const val EMPTY_JSON = "{ }"
        private const val INVALID_JSON = "{"
    }
}