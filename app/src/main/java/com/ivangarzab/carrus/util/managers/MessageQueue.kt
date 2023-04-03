package com.ivangarzab.carrus.util.managers

import android.os.Parcelable
import com.ivangarzab.carrus.data.MessageData
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
open class MessageQueue : Parcelable {

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
    fun pop(): MessageData = queue.removeFirst()
}