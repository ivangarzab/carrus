package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.util.managers.CarExporter
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarExporterModuleTest {

    @Test
    fun test_providing() {
        val result = CarExporterModule.provideCarExporter()
        assertThat(result).isNotNull()
        assertThat(result).isInstanceOf(CarExporter::class.java)
    }

    @Test
    fun test_singleton() {
        val result1 = CarExporterModule.provideCarExporter()
        val result2 = CarExporterModule.provideCarExporter()
        assertThat(result1)
            .isEqualTo(result2)
    }
}