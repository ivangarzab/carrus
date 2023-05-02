package com.ivangarzab.carrus.util.managers

import com.ivangarzab.carrus.data.MessageData
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
class UniqueMessageQueue: MessageQueue() {
    override fun add(data: MessageData) {
        if (contains(data.id)) {
            return // skip
        }
        super.add(data)
    }
}