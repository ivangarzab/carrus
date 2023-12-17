package com.ivangarzab.carrus.data.exceptions

import junit.framework.TestCase.assertEquals
import org.junit.Test

class MemoryLeakExceptionTest {

    @Test
    fun `test MemoryLeakException message`() {
        val errorMessage = "Memory Leak Detected!"
        val exception = MemoryLeakException(errorMessage)

        assertEquals(errorMessage, exception.message)
    }
}