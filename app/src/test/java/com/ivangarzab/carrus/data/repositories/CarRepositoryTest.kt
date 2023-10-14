package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.TEST_SERVICE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarRepositoryTest {

    private lateinit var repository: TestCarRepository

    @Before
    fun setup() {
        repository = TestCarRepository()
    }

    @Test
    fun test_observeCarData_base() = runTest {
        with(repository) {
            val carFlow = observeCarData()
            assertThat(carFlow).isInstanceOf(Flow::class.java)
            assertThat(carFlow.first()).isEqualTo(carData)
        }
    }

    @Test
    fun test_fetchCarData_base() = with(repository) {
        assertThat(fetchCarData())
            .isEqualTo(carData)
    }

    @Test
    fun test_saveCarData_base() = with(repository) {
        saveCarData(TEST_CAR)
        assertThat(carData)
            .isEqualTo(TEST_CAR)
    }

    @Test
    fun test_deleteCarData_base() = with(repository) {
        deleteCarData()
        assertThat(carData)
            .isNull()
    }

    @Test
    fun test_addCarService_base() = with(repository) {
        addCarService(TEST_SERVICE)
        assertThat(carData?.services?.contains(TEST_SERVICE))
            .isTrue()
    }

    @Test
    fun test_removeCarService_base() = with(repository) {
        addCarService(TEST_SERVICE)
        removeCarService(TEST_SERVICE)
        assertThat(carData?.services?.contains(TEST_SERVICE))
            .isFalse()
    }

    @Test
    fun test_removeCarService_service_dne() = with(repository) {
        val originalCarData = carData
        removeCarService(TEST_SERVICE)
        assertThat(carData)
            .isEqualTo(originalCarData)
    }

    @Test
    fun test_updateCarService_service_not_found() = with(repository) {
        updateCarService(TEST_SERVICE)
        assertThat(carData?.services?.contains(TEST_SERVICE))
            .isFalse()
    }

    @Test
    fun test_updateCarService_service_found() = with(repository) {
        addCarService(TEST_SERVICE)
        val updatedService = TEST_SERVICE.copy(name = "Updated Service")
        updateCarService(updatedService)
        assertThat(carData?.services?.contains(updatedService))
            .isTrue()
    }
}