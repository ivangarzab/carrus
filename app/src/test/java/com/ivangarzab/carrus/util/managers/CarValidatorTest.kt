package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.ivangarzab.carrus.STRING_BLANK
import com.ivangarzab.carrus.STRING_EMPTY
import com.ivangarzab.carrus.TEST_CAR_V0_JSON
import com.ivangarzab.carrus.TEST_CAR_V1_JSON
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
    fun test_validateCarData_valid_json_v0_return_valid_data() {
        val result = CarValidator.validateCarData(TEST_CAR_V0_JSON)
        assertThat(result)
            .isEquivalentAccordingToCompareTo(ANSWER_VALID_CAR_FROM_V0)
    }

    @Test
    fun test_validateCarData_valid_json_v1_return_valid_data() {
            val result = CarValidator.validateCarData(TEST_CAR_V1_JSON)
        assertThat(result)
            .isEquivalentAccordingToCompareTo(ANSWER_VALID_CAR_FROM_V1)
    }

    @Test
    fun test_upgradeCarJsonFromVersion0ToVersion1_valid_json() {
        val result = CarValidator.upgradeCarJsonFromVersion0ToVersion1(JSON_OBJECT)
        assertThat(result)
            .isEquivalentAccordingToCompareTo(ANSWER_VALID_CAR_FROM_V0.copy(version = 1))
    }

    companion object {
        private const val EMPTY_JSON = "{ }"
        private const val INVALID_JSON = "{"
        private val JSON_OBJECT = JsonObject().apply {
            addProperty("version", 0)
            addProperty("uid", "123")
            addProperty("nickname", "")
            addProperty("make", "Nissan")
            addProperty("model", "Altima")
            addProperty("year", "2012")
            addProperty("licenseNo", "DH9⭐L474")
            addProperty("vinNo", "ABCDEFGHI")
            addProperty("tirePressure", "32")
            addProperty("totalMiles", "100000")
            addProperty("milesPerGallon", "26")
            add("services", JsonArray())
        }
        private val ANSWER_VALID_CAR_FROM_V0 = Car(
            version = 0,
            uid = "123",
            nickname = "",
            make = "Nissan",
            model = "Altima",
            year = "2012",
            licenseState = "",
            licenseNo = "DH9⭐L474",
            vinNo = "ABCDEFGHI",
            tirePressure = "32",
            totalMiles = "100000",
            milesPerGalCity = "26",
            milesPerGalHighway = "26",
            imageUri = "",
            services = emptyList()
        )
        private val ANSWER_VALID_CAR_FROM_V1 = Car(
            version = 1,
            uid = "123",
            nickname = "",
            make = "Nissan",
            model = "Altima",
            year = "2012",
            licenseState = "Texas",
            licenseNo = "DH9⭐L474",
            vinNo = "ABCDEFGHI",
            tirePressure = "32",
            totalMiles = "100000",
            milesPerGalCity = "26",
            milesPerGalHighway = "26",
            imageUri = "",
            services = emptyList()
        )
    }
}