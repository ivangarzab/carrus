package com.ivangarzab.carrus.data.structures

import android.os.Parcelable
import com.ivangarzab.carrus.data.models.Message
import com.ivangarzab.carrus.data.models.MessageData
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
open class MessageQueue : Parcelable {

    @IgnoredOnParcel
    open var queue: MutableList<MessageData> = mutableListOf()

    fun size() = queue.size

    fun isEmpty() = size() == 0

    fun isNotEmpty(): Boolean = isEmpty().not()

    fun contains(id: String): Boolean {
        queue.forEach {
            if (it.id == id) {
                return true
            }
        }
        return false
    }

    fun remove(id: String): Boolean {
        queue.forEachIndexed { index, message ->
            if (message.id == id) {
                queue.removeAt(index)
                return true
            }
        }
        return false
    }

    open fun add(data: MessageData) {
        queue.add(data)
    }

    @Throws(NoSuchElementException::class)
    fun get(): MessageData = when (queue.isEmpty()) {
        true -> throw NoSuchElementException()
        false -> queue[0]
    }

    @Throws(NoSuchElementException::class)
    fun pop(): MessageData = queue.removeFirst()

    companion object {
        val test = MessageQueue().apply {
            add(Message.TEST.data)
        }
    }
}

fun MessageQueue.asUniqueMessageQueue() = UniqueMessageQueue()
    .apply {
        this@asUniqueMessageQueue
            .queue
            .forEach {
                add(it)
            }
    }