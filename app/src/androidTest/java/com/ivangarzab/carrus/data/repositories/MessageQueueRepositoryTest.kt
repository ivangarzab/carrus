package com.ivangarzab.carrus.data.repositories

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.ONE
import com.ivangarzab.carrus.TWO
import com.ivangarzab.carrus.ZERO
import com.ivangarzab.carrus.data.models.Message
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class MessageQueueRepositoryTest {

    private val repository = MessageQueueRepository()

    private val queue = repository.observeMessageQueueFlow().value

    @Test
    fun test_observeMessageQueueFlow_size_base() {
        assertThat(queue.size())
            .isEqualTo(ZERO)
    }

    @Test
    fun test_observeMessageQueueFlow_isEmpty_base() {
        assertThat(queue.isEmpty())
            .isTrue()
    }

    @Test
    fun test_addMessage_one_size_success() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            assertThat(observeMessageQueueFlow()
                .value
                .size()
            ).isEqualTo(ONE)
        }
    }

    @Test
    fun test_addMessage_one_equals_success() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            assertThat(queue.pop().id)
                .isEqualTo(it.data.id)
        }
    }

    @Test
    fun test_addMessage_size_two_success() = with(repository) {
        addMessage(Message.TEST)
        addMessage(Message.MISSING_PERMISSION_ALARM)
        assertThat(queue.size())
            .isEqualTo(TWO)
    }

    @Test
    fun test_addMessage_two_success() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            addMessage(Message.MISSING_PERMISSION_ALARM)
            assertThat(queue.pop().id)
                .isEqualTo(it.data.id)
        }
    }

    @Test
    fun test_addMessage_two_duplicate_size_failure() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            addMessage(it)
            assertThat(queue.size())
                .isEqualTo(ONE)
        }
    }

    @Test
    fun test_removeMessage_empty_list_size_base() = with(repository) {
        removeMessage(Message.TEST)
        assertThat(queue.size())
            .isEqualTo(ZERO)
    }

    @Test
    fun test_removeMessage_list_of_one_size_zero_success() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            removeMessage(it)
            assertThat(queue.size())
                .isEqualTo(ZERO)
        }
    }

    @Test
    fun test_removeMessage_list_of_two_size_one_success() = with(repository) {
        Message.TEST.let {
            addMessage(it)
            addMessage(Message.MISSING_PERMISSION_ALARM)
            removeMessage(it)
            assertThat(queue.size())
                .isEqualTo(ONE)
        }
    }

    @Test
    fun test_removeMessage_list_of_two_equals_success() = with(repository) {
        addMessage(Message.TEST)
        addMessage(Message.MISSING_PERMISSION_ALARM)
        removeMessage(Message.MISSING_PERMISSION_ALARM)
        assertThat(queue.pop().id)
            .isEqualTo(Message.TEST.data.id)
    }

    @Test
    fun test_removeMessage_list_of_two_size_one_failure() = with(repository) {
        addMessage(Message.TEST)
        addMessage(Message.MISSING_PERMISSION_ALARM)
        removeMessage(Message.TEST)
        assertThat(queue.pop().id)
            .isEqualTo(Message.MISSING_PERMISSION_ALARM.data.id)
    }

    @Test
    fun test_dismissMessage_empty_list_size_base() {
        assertThat(queue.size())
            .isEqualTo(ZERO)
    }

    @Test
    fun test_dismissMessage_list_of_one_size_zero_success() = with(repository) {
        addMessage(Message.TEST)
        dismissMessage()
        assertThat(queue.size())
            .isEqualTo(ZERO)
    }

    @Test
    fun test_dismissMessage_list_of_two_size_one_success() = with(repository) {
        addMessage(Message.TEST)
        addMessage(Message.MISSING_PERMISSION_ALARM)
        dismissMessage()
        assertThat(queue.size())
            .isEqualTo(ONE)
    }

    @Test
    fun test_dismissMessage_list_of_one_equal_success() = with(repository) {
        addMessage(Message.MISSING_PERMISSION_ALARM)
        addMessage(Message.TEST)
        dismissMessage()
        assertThat(queue.pop().id)
            .isEqualTo(Message.TEST.data.id)
    }

    @Test
    fun test_dismissMessage_list_of_one_equal_failure() = with(repository) {
        addMessage(Message.TEST)
        addMessage(Message.MISSING_PERMISSION_ALARM)
        dismissMessage()
        assertThat(queue.pop().id)
            .isNotEqualTo(Message.TEST.data.id)
    }
}