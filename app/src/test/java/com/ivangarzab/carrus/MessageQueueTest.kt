package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.MessageData
import com.ivangarzab.carrus.util.managers.MessageQueue
import com.ivangarzab.carrus.util.managers.MessageType
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

/**
 * Tests for [MessageQueue] class.
 *
 * Created by Ivan Garza Bermea.
 */
class MessageQueueTest {

    private var messageQueue = MessageQueue()

    @Before
    fun prep_queue() {
        messageQueue = MessageQueue()
    }

    @Test
    fun test_isNotEmpty_base() {
        assertEquals(FALSE, messageQueue.isNotEmpty())
    }

    @Test
    fun test_isNotEmpty_add_one() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(TRUE, messageQueue.isNotEmpty())
    }

    @Test
    fun test_isNotEmpty_add_two() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(TRUE, messageQueue.isNotEmpty())
    }

    @Test
    fun test_isNotEmpty_add_one_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.pop()
        assertEquals(FALSE, messageQueue.isNotEmpty())
    }

    @Test
    fun test_isEmpty_base() {
        assertEquals(TRUE, messageQueue.isEmpty())
    }

    @Test
    fun test_isEmpty_add_one() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(FALSE, messageQueue.isEmpty())
    }

    @Test
    fun test_isEmpty_add_two() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(FALSE, messageQueue.isEmpty())
    }

    @Test
    fun test_isEmpty_add_one_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.pop()
        assertEquals(TRUE, messageQueue.isEmpty())
    }

    @Test
    fun test_size_base() {
        assertEquals(ANSWER_0, messageQueue.size())
    }

    @Test
    fun test_size_add_one() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(ANSWER_1, messageQueue.size())
    }

    @Test
    fun test_size_add_one_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.pop()
        assertEquals(ANSWER_0, messageQueue.size())
    }

    @Test
    fun test_size_add_two() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(ANSWER_2, messageQueue.size())
    }

    @Test
    fun test_size_add_two_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.pop()
        assertEquals(ANSWER_1, messageQueue.size())
    }

    @Test(expected = NoSuchElementException::class)
    fun test_pop_base() {
        messageQueue.pop()
    }

    @Test
    fun test_add_one_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        assertEquals(
            TEST_MESSAGE_DATA_1,
            messageQueue.pop()
        )
    }

    @Test
    fun test_add_two_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1) // first
        messageQueue.add(TEST_MESSAGE_DATA_2) // second
        assertEquals(
            TEST_MESSAGE_DATA_1,
            messageQueue.pop()
        )
    }

    @Test
    fun test_add_three_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1) // first
        messageQueue.add(TEST_MESSAGE_DATA_2) // second
        messageQueue.add(MessageData(MessageType.INFO, "3")) // third
        assertEquals(
            TEST_MESSAGE_DATA_1,
            messageQueue.pop()
        )
    }

    @Test
    fun test_add_three_pop_pop() {
        messageQueue.add(TEST_MESSAGE_DATA_1) // first
        messageQueue.add(TEST_MESSAGE_DATA_2) // second
        messageQueue.add(MessageData(MessageType.INFO, "3")) // third
        messageQueue.pop()
        assertEquals(
            TEST_MESSAGE_DATA_2,
            messageQueue.pop()
        )
    }

    companion object {
        private const val ANSWER_0 = 0
        private const val ANSWER_1 = 1
        private const val ANSWER_2 = 2

        private val TEST_MESSAGE_DATA_1 = MessageData(
            type = MessageType.INFO,
            text = "One"
        )
        private val TEST_MESSAGE_DATA_2 = MessageData(
            type = MessageType.WARNING,
            text = "Two"
        )
    }
}