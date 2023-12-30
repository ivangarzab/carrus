package com.ivangarzab.carrus.util.managers

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.models.Car
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarExporterTest {

    private val exporter = CarExporter()

    @Test
    fun test_exportFromJson_empty_car_data_not_null() {
        assertThat(exporter.exportToJson(Car.empty))
            .isNotNull()
    }

    @Test
    fun test_exportFromJson_car_data_without_image_not_null() {
        assertThat(exporter.exportToJson(Car.default))
            .isNotNull()
    }

    @Test
    fun test_exportFromJson_car_data_without_image_no_valid_imageUri() = with(
        exporter.exportToJson(Car.default)
    ) {
        assertThat(this?.let { isCarJsonImageUriNull(it) })
            .isTrue()
    }

    @Test
    fun test_exportFromJson_car_data_with_image_not_null() = with(
        exporter.exportToJson(Car.default.copy(imageUri = IMAGE_URI))
    ) {
        assertThat(this)
            .isNotNull()
    }

    @Test
    fun test_exportFromJson_car_data_with_image_no_valid_imageUri() = with(
        exporter.exportToJson(Car.default.copy(imageUri = IMAGE_URI))
    ) {
        assertThat(this?.let { isCarJsonImageUriNull(it) })
            .isTrue()
    }

    private fun isCarJsonImageUriNull(json: String): Boolean {
        return if (json.contains("\"imageUri\"")) {
            json.contains("\"imageUri\": null")
        } else true
    }

    companion object {
        private const val IMAGE_URI = "this_is_some_image_uri"
    }
}