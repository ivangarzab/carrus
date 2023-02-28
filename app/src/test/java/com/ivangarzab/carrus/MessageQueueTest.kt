package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.MessageData
import com.ivangarzab.carrus.util.managers.MessageQueue
import com.ivangarzab.carrus.util.managers.MessageType
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class MessageQueueTest {

    private var messageQueue = MessageQueue()

    @Before
    fun prep_queue() {
        messageQueue = MessageQueue()
    }

    @Test
    fun test_size_base() {
        assertEquals(0, messageQueue.size())
    }

    @Test
    fun test_isNotEmpty_base() {
        assertEquals(false, messageQueue.isNotEmpty())
    }

    @Test
    fun test_size_add_one() {
        messageQueue.add(TEST_MESSAGE_DATA)
        assertEquals(1, messageQueue.size())
    }

    @Test
    fun test_isNotEmpty_add_one() {
        messageQueue.add(TEST_MESSAGE_DATA)
        assertEquals(true, messageQueue.isNotEmpty())
    }

    companion object {
        private val TEST_MESSAGE_DATA = MessageData(
            type = MessageType.INFO,
            text = "Test Message"
        )
    }
}