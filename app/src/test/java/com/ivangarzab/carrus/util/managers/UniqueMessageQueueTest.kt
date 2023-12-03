package com.ivangarzab.carrus.util.managers

import com.ivangarzab.carrus.data.structures.UniqueMessageQueue
import com.ivangarzab.test_data.ONE
import com.ivangarzab.test_data.TEST_MESSAGE_DATA_1
import com.ivangarzab.test_data.TEST_MESSAGE_DATA_2
import com.ivangarzab.test_data.TWO
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
        TestCase.assertEquals(ONE, messageQueue.size())
    }

    @Test
    fun `test unique add +2 = size 2`() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_2)
        TestCase.assertEquals(TWO, messageQueue.size())
    }

    @Test
    fun `test same add +2 = size 1`() {
        messageQueue.add(TEST_MESSAGE_DATA_1)
        messageQueue.add(TEST_MESSAGE_DATA_1)
        TestCase.assertEquals(ONE, messageQueue.size())
    }
}