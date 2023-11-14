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
            title = "Aversion hope evil decrepit value society grandeur moral. Self pious value noble christian snare ocean horror battle reason decieve abstract war.",
            body = "Derive intentions ocean ubermensch prejudice. Pious transvaluation society justice sea evil convictions sea insofar madness fearful Zarathustra.",
            iconRes = R.drawable.ic_mark_i
        )
    ),
    MISSING_PERMISSION_NOTIFICATION(
        MessageData(
            id = "100",
            type = MessageType.WARNING,
            title = "Stay on the loop!",
            body = "Please grant us notification permissions to maximize your experience and let us notify you.",
            iconRes = R.drawable.ic_notification
        )
    ),
    MISSING_PERMISSION_ALARM(
        MessageData(
            id = "101",
            type = MessageType.WARNING,
            title = "Let us notify you!",
            body = "Please grant us alarm permissions so that we can notify you when a service is due.",
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
    val title: String,
    val body: String,
    val iconRes: Int
)