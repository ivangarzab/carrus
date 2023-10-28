package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.EMPTY_CAR
import com.ivangarzab.carrus.data.SERVICE_EMPTY
import com.ivangarzab.carrus.data.SERVICE_TEST_1
import com.ivangarzab.carrus.data.SERVICE_TEST_2
import com.ivangarzab.carrus.data.TEST_CAR
import com.ivangarzab.carrus.prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


/**
 * Created by Ivan Garza Bermea.
 */
class CarRepositoryTest {

    private val repository = CarRepositoryImpl()

    @Before
    fun setup() {
        prefs.defaultCar = null
    }

    @Test
    fun test_base_control() = with(repository) {
        assertThat(fetchCarData())
            .isNull()
    }

    @Test
    fun test_observeCarData_base() = runTest {
        with(repository) {
            val carFlow = observeCarData()
            assertThat(carFlow).isInstanceOf(Flow::class.java)
        }
    }

    @Test
    fun test_fetchCarData_success() = with(repository) {
        prefs.defaultCar = TEST_CAR
        assertThat(fetchCarData())
            .isEqualTo(TEST_CAR)
    }

    @Test
    fun test_saveCarData_success() = with(repository) {
        saveCarData(TEST_CAR)
        assertThat(fetchCarData())
            .isEqualTo(TEST_CAR)
    }

    @Test
    fun test_saveCarData_failure() = with(repository) {
        saveCarData(TEST_CAR)
        assertThat(fetchCarData())
            .isNotEqualTo(EMPTY_CAR)
    }

    @Test
    fun test_deleteCarData_control() = with(repository) {
        saveCarData(TEST_CAR)
        deleteCarData()
        assertThat(fetchCarData())
            .isNull()
    }
    
    @Test
    fun test_empty_service_list_control() = with(repository) {
        saveCarData(TEST_CAR)
        assertThat(fetchCarData()?.services)
            .isEmpty()
    }

    @Test
    fun test_addCarService_non_empty_list() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .isNotEmpty()
    }

    @Test
    fun test_addCarService_contains_success() = with(repository) {
        saveCarData(TEST_CAR)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        assertThat(fetchCarData()?.services)
            .contains(expected)
    }

    @Test
    fun test_addCarService_equals_success() = with(repository) {
        saveCarData(TEST_CAR)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        assertThat(fetchCarData()?.services?.get(0))
            .isEqualTo(expected)
    }

    @Test
    fun test_addCarService_fail() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_EMPTY)
    }

    @Test
    fun test_removeCarService_doesNotContain_success() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        removeCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_TEST_1)
    }

    @Test
    fun test_removeCarService_empty_list_success() = with(repository) {
        saveCarData(TEST_CAR)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        removeCarService(expected)
        assertThat(fetchCarData()?.services)
            .isEmpty()
    }

    @Test
    fun test_removeCarService_fail() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        removeCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_EMPTY)
    }

    @Test
    fun test_updateCarService_success() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        val expected = SERVICE_TEST_1.copy( brand = "newBrand" )
        updateCarService(expected)
        assertThat(fetchCarData()?.services?.get(0))
            .isEqualTo(expected)
    }

    @Test
    fun test_updateCarService_fail() = with(repository) {
        saveCarData(TEST_CAR)
        addCarService(SERVICE_TEST_1)
        updateCarService(SERVICE_TEST_1.copy( brand = "newBrand" ))
        assertThat(fetchCarData()?.services?.get(0))
            .isNotEqualTo(SERVICE_TEST_2)
    }
}