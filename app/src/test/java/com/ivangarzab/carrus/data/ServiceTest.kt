package com.ivangarzab.carrus.data

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.util.extensions.getFormattedDate
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class ServiceTest {

    @Test
    fun testCompareTo() {
        // Create two services with different IDs.
        val service1 = Service(
            id = "1",
            name = "Service 1",
            repairDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
            dueDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
        )
        val service2 = Service(
            id = "2",
            name = "Service 2",
            repairDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
            dueDate = Calendar.getInstance().apply { timeInMillis = 1699882020000 },
        )

        // Assert that service1 comes before service2.
        assertEquals(-1, service1.compareTo(service2))

        // Assert that service2 comes after service1.
        assertEquals(1, service2.compareTo(service1))
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