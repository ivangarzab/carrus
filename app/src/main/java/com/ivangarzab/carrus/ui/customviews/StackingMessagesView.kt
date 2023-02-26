package com.ivangarzab.carrus.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ivangarzab.carrus.databinding.ItemMessageBinding
import com.ivangarzab.carrus.util.extensions.bind
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class StackingMessagesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    enum class MessageType {
        INFO,
        WARNING,
    }

    data class MessageData(
        val type: MessageType,
        val text: String
    )

    private val layoutInflater: LayoutInflater

    private var messageQueue: List<MessageData> = emptyList()

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    fun addMessage(data: MessageData) {
        messageQueue = messageQueue.toMutableList().apply {
            add(data)
        }.toList()
        showMessageIfPossible()
    }

    private fun showMessageIfPossible() {
        if (messageQueue.isNotEmpty()) {
            val message = messageQueue[0]
            addView(
                ItemMessageBinding.inflate(
                    layoutInflater,
                    this,
                    false
                ).apply {
                    bind(message.text) {
                        removeAllViews()
                    }
                }.root
            )
        } else {
            Timber.v("There are no messages to show")
        }
    }
}