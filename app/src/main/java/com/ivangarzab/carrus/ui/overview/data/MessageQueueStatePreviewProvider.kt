package com.ivangarzab.carrus.ui.overview.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.data.Message

/**
 * Created by Ivan Garza Bermea.
 */
class MessageQueueStatePreviewProvider : PreviewParameterProvider<MessageQueueState> {
    override val values = sequenceOf(
        MessageQueueState().apply {
            messageQueue.add(Message.TEST.data)
        }
    )
}