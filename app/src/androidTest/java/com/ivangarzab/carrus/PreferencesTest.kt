package com.ivangarzab.carrus

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.ivangarzab.carrus.data.*
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
@Suppress("ReplaceGetOrSet")
class PreferencesTest {
    private var preferences: SharedPreferences =
        InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
            TEST_PREF,
            Context.MODE_PRIVATE
        )

    /**
     * Test String -- Success
     */
    @Test
    fun test_sharedPreferences_string_success() {
        val test: String = TEST_STRING
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, DEFAULT_STRING).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test String -- Failure
     */
    @Test
    fun test_sharedPreferences_string_fail() {
        val test: String = TEST_STRING
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, DEFAULT_STRING).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test Int -- Success
     */
    @Test
    fun test_sharedPreferences_int_success() {
        val test: Int = TEST_INT
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, DEFAULT_INT).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test Int -- Failure
     */
    @Test
    fun test_sharedPreferences_int_fail() {
        val test: Int = TEST_INT
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, DEFAULT_INT).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test Float -- Success
     */
    @Test
    fun test_sharedPreferences_float_success() {
        val test: Float = TEST_FLOAT
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, DEFAULT_FLOAT).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test Float -- Failure
     */
    @Test
    fun test_sharedPreferences_float_fail() {
        val test: Float = TEST_FLOAT
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, DEFAULT_FLOAT).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test Long -- Success
     */
    @Test
    fun test_sharedPreferences_long_success() {
        val test: Long = TEST_LONG
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, DEFAULT_LONG).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test Long -- Failure
     */
    @Test
    fun test_sharedPreferences_long_fail() {
        val test: Long = TEST_LONG
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, DEFAULT_LONG).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test Boolean -- Success
     */
    @Test
    fun test_sharedPreferences_boolean_success() {
        val test: Boolean = TEST_BOOLEAN
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, DEFAULT_BOOLEAN).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test Boolean -- Failure
     */
    @Test
    fun test_sharedPreferences_boolean_fail() {
        val test: Boolean = TEST_BOOLEAN
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, DEFAULT_BOOLEAN).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test Car -- Success
     */
    @Test
    fun test_sharedPreferences_car_success() {
        val test: Car = Car.empty
        preferences.set(TEST_KEY, test)
        preferences.get(TEST_KEY, Car.default).let { result ->
            Assert.assertEquals(test, result)
        }
    }

    /**
     * Test Car -- Failure
     */
    @Test
    fun test_sharedPreferences_car_fail() {
        val test: Car = Car.default
        preferences.set(TEST_KEY, test)
        preferences.get(FAILURE_KEY, Car.empty).let { result ->
            Assert.assertNotEquals(test, result)
        }
    }

    /**
     * Test [UnsupportedOperationException] by passing 'null'
     */
    @Test(expected = UnsupportedOperationException::class)
    fun test_sharedPreferences_unsupported_operation_exception_null() {
        preferences.set(TEST_KEY, TEST_STRING) // set a String
        preferences.get(TEST_KEY, null) // get a non-String
    }

    /**
     * Test [UnsupportedOperationException] by using a non-native object type
     */
    @Test(expected = UnsupportedOperationException::class)
    fun test_sharedPreferences_unsupported_operation_exception_object() {
        val test = TEST_SERVICE
        preferences.set(TEST_KEY, test) // set a non-native object
    }

    /**
     * Test [ClassCastException]
     */
    @Test(expected = ClassCastException::class)
    fun test_sharedPreferences_class_cast_exception() {
        preferences.set(TEST_KEY, TEST_STRING) // set a String
        preferences.get(TEST_KEY, TEST_INT) // get an Int
    }

    companion object {
        // shared prefs
        private const val TEST_PREF: String = "test-pref-file"
        private const val TEST_KEY: String = "test-pref-key"
        private const val FAILURE_KEY: String = "test-failure-pref-key"
        // test values
        private const val TEST_STRING: String = "test"
        private const val TEST_INT: Int = 6
        private const val TEST_FLOAT: Float = 6f
        private const val TEST_LONG: Long = 6L
        private const val TEST_BOOLEAN: Boolean = true
        private val TEST_SERVICE: Service = Service(
            name = "test",
            repairDate = Calendar.getInstance(),
            dueDate = Calendar.getInstance()
        )
        // default values
        private const val DEFAULT_STRING: String = ""
        private const val DEFAULT_INT: Int = -1
        private const val DEFAULT_FLOAT: Float = -1f
        private const val DEFAULT_LONG: Long = -1L
        private const val DEFAULT_BOOLEAN: Boolean = false
    }
}