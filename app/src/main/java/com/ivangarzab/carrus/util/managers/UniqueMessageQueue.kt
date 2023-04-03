package com.ivangarzab.carrus.util.managers

import com.ivangarzab.carrus.data.MessageData

/**
 * Created by Ivan Garza Bermea.
 */
class UniqueMessageQueue: MessageQueue() {
    override fun add(data: MessageData) {
        if (contains(data.id)) {
            return // skip
        }
        super.add(data)
    }
}