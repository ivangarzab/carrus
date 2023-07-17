package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.TEST_CAR_JSON
import com.ivangarzab.carrus.data.Car
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarImporterTest {

    @Test
    fun test_importFromJson_empty_string_return_null() {
        assertThat(CarImporter.importFromJson(TEST_EMPTY_CAR_DATA))
            .isNull()
    }

    @Test
    fun test_importFromJson_bad_string_return_null() {
        assertThat(CarImporter.importFromJson(TEST_BAD_CAR_DATA))
            .isNull()
    }

    @Test
    fun test_importFromJson_success() {
        assertThat(CarImporter.importFromJson(TEST_CAR_JSON))
            .isInstanceOf(Car::class.java)
    }

    companion object {
        private const val TEST_EMPTY_CAR_DATA = ""
        private const val TEST_BAD_CAR_DATA = "this is bad car data"

    }
}