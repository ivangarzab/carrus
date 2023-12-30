package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth
import com.ivangarzab.carrus.util.managers.CarImporter
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarImporterModuleTest {

    @Test
    fun test_providing() {
        val result = CarImporterModule.provideCarImporter()
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isInstanceOf(CarImporter::class.java)
    }

    @Test
    fun test_singleton() {
        val result1 = CarImporterModule.provideCarImporter()
        val result2 = CarImporterModule.provideCarImporter()
        Truth.assertThat(result1)
            .isEqualTo(result2)
    }
}