package com.ivangarzab.carrus.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ivangarzab.carrus.databinding.ItemMessageBinding
import com.ivangarzab.carrus.util.extensions.bind
import com.ivangarzab.carrus.util.managers.MessageData
import com.ivangarzab.carrus.util.managers.MessageQueue
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class StackingMessagesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val layoutInflater: LayoutInflater

    private val messageQueue: MessageQueue

    init {
        layoutInflater = LayoutInflater.from(context)
        messageQueue = MessageQueue()
    }

    fun addMessage(data: MessageData) {
        messageQueue.add(data)
        processMessageQueue()
    }

    private fun processMessageQueue() {
        if (messageQueue.isNotEmpty()) {
            try {
                showMessage(messageQueue.pop())
            } catch (e: NoSuchElementException) {
                Timber.w("Unable to get next available message from queue")
            }
        } else {
            Timber.v("There are no messages to process in queue")
        }
    }

    private fun showMessage(message: MessageData) {
        Timber.v("Showing message: $message")
        addView(
            ItemMessageBinding.inflate(
                layoutInflater,
                this,
                false
            ).apply {
                bind(message.text) { onMessageDismissed() }
            }.root
        )
    }

    private fun onMessageDismissed() {
        // TODO: Polish
        removeAllViews()
    }
}