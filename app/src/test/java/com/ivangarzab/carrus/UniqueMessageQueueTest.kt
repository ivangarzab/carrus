package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.UniqueMessageQueue
import junit.framework.TestCase
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class UniqueMessageQueueTest {

    private var messageQueue = UniqueMessageQueue()

    @Test
    fun `test unique add +1 = size 1`() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        TestCase.assertEquals(ANSWER_1, messageQueue.size())
    }

    @Test
    fun `test unique add +2 = size 2`() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_2)
        TestCase.assertEquals(ANSWER_2, messageQueue.size())
    }

    @Test
    fun `test same add +2 = size 1`() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        TestCase.assertEquals(ANSWER_1, messageQueue.size())
    }
}