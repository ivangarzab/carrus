package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.EMPTY_CAR
import com.ivangarzab.carrus.TEST_CAR
import com.ivangarzab.carrus.TEST_SERVICE
import com.ivangarzab.carrus.data.models.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CarRepositoryTest {

    private lateinit var repository: TestCarRepository
    private var value: Car? = null

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = TestCarRepository()
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun populateData() {
        repository._carDataChannel.value = EMPTY_CAR.copy(
            services = emptyList()
        )
    }

    @Test
    fun test_observeCarData_base() = runTest {
        with(repository) {
            val carFlow = observeCarData()
            assertThat(carFlow).isInstanceOf(Flow::class.java)
            assertThat(carFlow.first()).isNull()
        }
    }

    @Test
    fun test_observeCarData_with_data() = runTest {
        with(repository) {
            populateData()
            val carFlow = observeCarData()
            val expected = carDataChannel.value
            assertThat(carFlow).isInstanceOf(Flow::class.java)
            assertThat(carFlow.first()).isEqualTo(expected)
        }
    }

    @Test
    fun test_fetchCarData_base() = runTest {
        with(repository) {
            val expected = carDataChannel.value
            assertThat(fetchCarData())
                .isEqualTo(expected)
        }
    }

    @Test
    fun test_saveCarData_base() = runTest {
        with(repository) {
            saveCarData(TEST_CAR)
            val result = carDataChannel.first()
            assertThat(result)
                .isEqualTo(TEST_CAR)
        }
    }

    @Test
    fun test_deleteCarData_base() = runTest {
        with(repository) {
            deleteCarData()
            val result = carDataChannel.value
            assertThat(result)
                .isNull()
        }
    }

    @Test
    fun test_addCarService_base() = runTest {
        with(repository) {
            populateData()
            addCarService(TEST_SERVICE)
            val result = repository.carDataChannel.value
            assertThat(result?.services?.contains(TEST_SERVICE))
                .isTrue()
        }
    }

    @Test
    fun test_removeCarService_base() = runTest {
        with(repository) {
            populateData()
            addCarService(TEST_SERVICE)
            removeCarService(TEST_SERVICE)
            val result = repository.carDataChannel.value
            assertThat(result?.services?.contains(TEST_SERVICE))
                .isFalse()
        }
    }

    @Test
    fun test_removeCarService_service_dne() = runTest {
        with(repository) {
            val originalCarData = repository.carDataChannel.value
            removeCarService(TEST_SERVICE)
            val result = repository.carDataChannel.value
            assertThat(result)
                .isEqualTo(originalCarData)
        }
    }

    @Test
    fun test_updateCarService_service_not_found() = runTest {
        with(repository) {
            populateData()
            updateCarService(TEST_SERVICE)
            val result = repository.carDataChannel.value
            assertThat(result?.services?.contains(TEST_SERVICE))
                .isFalse()
        }
    }

    @Test
    fun test_updateCarService_service_found() = runTest {
        with(repository) {
            populateData()
            addCarService(TEST_SERVICE)
            val updatedService = TEST_SERVICE.copy(name = TEST_SERVICE_UPDATED_NAME)
            updateCarService(updatedService)

            val result = repository.carDataChannel.value
            assertThat(result?.services?.contains(updatedService))
                .isTrue()
        }
    }

    companion object {
        private const val TEST_SERVICE_UPDATED_NAME = "Updated Service"
    }
}