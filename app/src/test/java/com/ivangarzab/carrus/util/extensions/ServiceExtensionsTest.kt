package com.ivangarzab.carrus.util.extensions

import com.ivangarzab.test_data.TEST_SERVICE
import com.ivangarzab.test_data.TEST_SERVICE_EMPTY
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class ServiceExtensionsTest {

    @Test
    fun test_isPastDue_false() {
        with(TEST_SERVICE.copy(dueDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2099)
        })) {
            assertEquals(false, isPastDue())
        }
    }

    @Test
    fun test_isPastDue_true() {
        with(TEST_SERVICE.copy(dueDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1992)
        })) {
            assertEquals(true, isPastDue())
        }
    }

    @Test
    fun test_getDetails_brand_and_type() {
        with(TEST_SERVICE) {
            assertEquals("$brand - $type", getDetails())
        }
    }

    @Test
    fun test_getDetails_brand_only() {
        "brand".let {
            with(TEST_SERVICE_EMPTY.copy(brand = it)) {
                assertEquals(it, getDetails())
            }
        }
    }

    @Test
    fun test_getDetails_type_only() {
        "type".let {
            with(TEST_SERVICE_EMPTY.copy(brand = it)) {
                assertEquals(it, getDetails())
            }
        }
    }

    @Test
    fun test_getDetails_no_brand_or_type() {
        with(TEST_SERVICE_EMPTY) {
            assertEquals("---", getDetails())
        }
    }
}