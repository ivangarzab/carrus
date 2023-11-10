package com.ivangarzab.carrus.data.models

import com.ivangarzab.carrus.R

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
            text = "This is our first test message inside the stacking layout!",
            iconRes = R.drawable.ic_mark_i
        )
    ),
    MISSING_PERMISSION_NOTIFICATION(
        MessageData(
            id = "100",
            type = MessageType.WARNING,
            text = "Please grant us notification permissions to maximize your experience.",
            iconRes = R.drawable.ic_notification
        )
    ),
    MISSING_PERMISSION_ALARM(
        MessageData(
            id = "101",
            type = MessageType.WARNING,
            text = "Please grant us alarm permissions so that we can notify you when a service is due.",
            iconRes = R.drawable.ic_alarm
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
    val text: String,
    val iconRes: Int
)