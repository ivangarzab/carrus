package com.ivangarzab.carrus.util.managers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
class MessageQueue : Parcelable {
    private var queue: MutableList<MessageData> = mutableListOf()

    fun size() = queue.size

    fun isEmpty() = size() == 0

    fun isNotEmpty(): Boolean = isEmpty().not()

    fun add(data: MessageData) {
        queue.add(data)
    }

    @Throws(NoSuchElementException::class)
    fun pop(): MessageData = queue.removeFirst()
}