package com.ivangarzab.carrus.util.managers

/**
 * Created by Ivan Garza Bermea.
 */

enum class MessageType {
    INFO,
    WARNING,
}

data class MessageData(
    val type: MessageType,
    val text: String
)

class MessageQueue {
    private var queue: MutableList<MessageData> = mutableListOf()

    fun size() = queue.size

    fun isNotEmpty(): Boolean = queue.size != 0

    fun add(data: MessageData) {
        queue.add(data)
    }

    @Throws(NoSuchElementException::class)
    fun pop(): MessageData = queue.removeFirst()
}