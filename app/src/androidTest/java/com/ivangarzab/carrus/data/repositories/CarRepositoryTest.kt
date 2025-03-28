package com.ivangarzab.carrus.data.repositories

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.providers.DebugFlagProviderImpl
import com.ivangarzab.carrus.util.managers.Preferences
import com.ivangarzab.test_data.CAR_EMPTY
import com.ivangarzab.test_data.CAR_TEST
import com.ivangarzab.test_data.SERVICE_EMPTY
import com.ivangarzab.test_data.SERVICE_TEST_1
import com.ivangarzab.test_data.SERVICE_TEST_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


/**
 * Created by Ivan Garza Bermea.
 */
class CarRepositoryTest {

    private val prefs: Preferences = Preferences(
        InstrumentationRegistry.getInstrumentation().context,
        DebugFlagProviderImpl().apply { forceDebug = true }
    )

    private val repository = CarRepositoryImpl(
        CoroutineScope(SupervisorJob() + Dispatchers.Default),
        prefs
    )

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
        prefs.defaultCar = CAR_TEST
        assertThat(fetchCarData())
            .isEqualTo(CAR_TEST)
    }

    @Test
    fun test_saveCarData_success() = with(repository) {
        saveCarData(CAR_TEST)
        assertThat(fetchCarData())
            .isEqualTo(CAR_TEST)
    }

    @Test
    fun test_saveCarData_failure() = with(repository) {
        saveCarData(CAR_TEST)
        assertThat(fetchCarData())
            .isNotEqualTo(CAR_EMPTY)
    }

    @Test
    fun test_deleteCarData_control() = with(repository) {
        saveCarData(CAR_TEST)
        deleteCarData()
        assertThat(fetchCarData())
            .isNull()
    }
    
    @Test
    fun test_empty_service_list_control() = with(repository) {
        saveCarData(CAR_TEST)
        assertThat(fetchCarData()?.services)
            .isEmpty()
    }

    @Test
    fun test_addCarService_non_empty_list() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .isNotEmpty()
    }

    @Test
    fun test_addCarService_contains_success() = with(repository) {
        saveCarData(CAR_TEST)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        assertThat(fetchCarData()?.services)
            .contains(expected)
    }

    @Test
    fun test_addCarService_equals_success() = with(repository) {
        saveCarData(CAR_TEST)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        assertThat(fetchCarData()?.services?.get(0))
            .isEqualTo(expected)
    }

    @Test
    fun test_addCarService_fail() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_EMPTY)
    }

    @Test
    fun test_removeCarService_doesNotContain_success() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        removeCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_TEST_1)
    }

    @Test
    fun test_removeCarService_empty_list_success() = with(repository) {
        saveCarData(CAR_TEST)
        val expected = SERVICE_TEST_1
        addCarService(expected)
        removeCarService(expected)
        assertThat(fetchCarData()?.services)
            .isEmpty()
    }

    @Test
    fun test_removeCarService_fail() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        removeCarService(SERVICE_TEST_1)
        assertThat(fetchCarData()?.services)
            .doesNotContain(SERVICE_EMPTY)
    }

    @Test
    fun test_updateCarService_success() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        val expected = SERVICE_TEST_1.copy( brand = "newBrand" )
        updateCarService(expected)
        assertThat(fetchCarData()?.services?.get(0))
            .isEqualTo(expected)
    }

    @Test
    fun test_updateCarService_fail() = with(repository) {
        saveCarData(CAR_TEST)
        addCarService(SERVICE_TEST_1)
        updateCarService(SERVICE_TEST_1.copy( brand = "newBrand" ))
        assertThat(fetchCarData()?.services?.get(0))
            .isNotEqualTo(SERVICE_TEST_2)
    }
}