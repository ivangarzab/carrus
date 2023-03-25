package com.ivangarzab.carrus.util.managers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */

enum class Message(
    val data: MessageData
) {
    TEST(
        MessageData(
            id = "001",
            type = MessageType.INFO,
            text = "This is our first test message inside the stacking layout!"
        )
    ),
    MISSING_PERMISSION_NOTIFICATION(
        MessageData(
            id = "100",
            type = MessageType.WARNING,
            text = "Please grant us notification permissions to maximize your experience."
        )
    )
}

enum class MessageType {
    INFO,
    WARNING,
}

data class MessageData(
    val id: String,
    val type: MessageType,
    val text: String
)

@Parcelize
class MessageQueue : Parcelable {
    private var queue: MutableList<MessageData> = mutableListOf()

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

    fun add(data: MessageData) {
        queue.add(data)
    }

    @Throws(NoSuchElementException::class)
    fun pop(): MessageData = queue.removeFirst()
}