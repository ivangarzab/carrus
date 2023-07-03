package com.ivangarzab.carrus.data.repositories

import com.ivangarzab.carrus.util.managers.MessageQueue
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by Ivan Garza Bermea.
 */
class MessageQueueRepository {

    private val messageQueueFlow = MutableStateFlow(MessageQueue())
}