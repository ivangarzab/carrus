package com.ivangarzab.carrus.data

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
    ),
    MISSING_PERMISSION_ALARM(
        MessageData(
            id = "101",
            type = MessageType.WARNING,
            text = "Please grant us alarm permissions so that we can notify you when a service is due."
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