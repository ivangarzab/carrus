package com.ivangarzab.carrus.data.models

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.util.extensions.getFormattedDate
import com.ivangarzab.test_data.data.COMPARE_TO_EQUALS
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class ServiceTest {

    @Test
    fun test_compareTo_failure() {
        // Create two services with different IDs.
        val service1 = Service(
            id = "1",
            name = "Service 1",
            repairDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
            dueDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
            brand = "brand1",
            type = "type1",
            cost = 0.00f
        )
        val service2 = Service(
            id = "2",
            name = "Service 2",
            repairDate = Calendar.getInstance().apply { timeInMillis = 1699882030000 },
            dueDate = Calendar.getInstance().apply { timeInMillis = 1699882030000 },
            brand = "brand2",
            type = "type2",
            cost = 9.99f
        )

        assertThat(service1.compareTo(service2))
            .isNotEqualTo(COMPARE_TO_EQUALS)
    }

    @Test
    fun test_compareTo_success() {
        // Create two services with different IDs.
        val service1 = Service(
            id = "1",
            name = "Oil Change",
            repairDate = Calendar.getInstance().apply { timeInMillis = 1639120980000 },
            dueDate = Calendar.getInstance().apply { timeInMillis = 1672590100000 },
            brand = "Armor All",
            type = "Synthetic",
            cost = 79.99f
        )

        assertThat(Service.serviceList[0].compareTo(service1))
            .isEqualTo(COMPARE_TO_EQUALS)
    }

    @Test
    fun testToString() {
        // Create a service and assert that its toString() method returns the expected string.
        val service = Service(id = "1", name = "Service 1", repairDate = Calendar.getInstance(), dueDate = Calendar.getInstance())
        assertThat(service.toString())
            .isEqualTo("Service(\n" +
                    "name='Service 1'\n" +
                    "repairDate='${service.repairDate.getFormattedDate()}'\n" +
                    "dueDate='${service.dueDate.getFormattedDate()}'\n" +
                    "brand='null'\n" +
                    "type='null'\n" +
                    "cost='0.0'\n" +
                    ")"
            )
    }

    @Test
    fun testGetEmptyService() {
        // Assert that the empty service has the expected values.
        val TEST_ID = "-1"
        val TEST_NAME = "EMPTY"
        val TEST_CALENDAR = Calendar.getInstance().apply { timeInMillis = 0L }
        val emptyService = Service(
            id = TEST_ID,
            name = TEST_NAME,
            repairDate = TEST_CALENDAR,
            dueDate = TEST_CALENDAR,
        )
        assertEquals(TEST_ID, emptyService.id)
        assertEquals(TEST_NAME, emptyService.name)
        assertEquals(TEST_CALENDAR, emptyService.repairDate)
        assertEquals(TEST_CALENDAR, emptyService.dueDate)
        assertEquals(null, emptyService.brand)
        assertEquals(null, emptyService.type)
        assertEquals(0.00f, emptyService.cost)
    }
}