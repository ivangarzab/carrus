package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.test_data.TEST_CAR_JSON
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarImporterTest {

    private val importer = CarImporter()

    @Test
    fun test_importFromJson_empty_string_null() {
        assertThat(importer.importFromJson(TEST_EMPTY_CAR_DATA))
            .isNull()
    }

    @Test
    fun test_importFromJson_bad_string_null() {
        assertThat(importer.importFromJson(TEST_BAD_CAR_DATA))
            .isNull()
    }

    @Test
    fun test_importFromJson_success() {
        assertThat(importer.importFromJson(TEST_CAR_JSON))
            .isInstanceOf(Car::class.java)
    }

    @Test
    fun test_importFromJson_success_no_imageUri() = with(
        importer.importFromJson(TEST_CAR_JSON)
    ) {
        assertThat(this?.imageUri)
            .isNull()
    }

    companion object {
        private const val TEST_EMPTY_CAR_DATA = ""
        private const val TEST_BAD_CAR_DATA = "this is bad car data"
    }
}