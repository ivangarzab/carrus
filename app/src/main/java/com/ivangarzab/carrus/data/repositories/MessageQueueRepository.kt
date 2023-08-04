package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.data.Message
import com.ivangarzab.carrus.util.managers.MessageQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class MessageQueueRepository @Inject constructor() {

    private val messageQueueFlow = MutableStateFlow(MessageQueue())

    fun observeMessageQueueFlow() = messageQueueFlow.asStateFlow()

    fun addMessage(message: Message) = with(message.data) {
        messageQueueFlow.value.let {
            if (it.contains(message.data.id)) {
                return // skip dupes
            }
            Timber.d("Added message with id=${id} from queue")
            updateMessageQueue(it.apply { add(message.data) })
        }
    }

    fun removeMessage(message: Message) = with(message.data) {
        messageQueueFlow.value.let {
            if (it.contains(id)) {
                Timber.d("Removed message with id=${id} from queue")
                updateMessageQueue(it.apply { remove(id) })
            }
        }
    }

    fun dismissMessage() {
        updateMessageQueue(messageQueueFlow.value.apply {
            Timber.d("Popped message with id=${pop().id} from queue")
        })
    }

    private fun updateMessageQueue(messageQueue: MessageQueue) {
        messageQueueFlow.value = MessageQueue().apply {
            queue = messageQueue.queue
        }
    }
}