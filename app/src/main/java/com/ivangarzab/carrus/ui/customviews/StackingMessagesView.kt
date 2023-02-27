package com.ivangarzab.carrus.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ivangarzab.carrus.databinding.ItemMessageBinding
import com.ivangarzab.carrus.databinding.ViewStackingMessagesBinding
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
) : ConstraintLayout(context, attrs, defStyle) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val binding = ViewStackingMessagesBinding.inflate(
        layoutInflater,
        this,
        true
    )

    private val messageQueue: MessageQueue = MessageQueue()

    fun addMessage(data: MessageData) {
        Timber.v("Got a new message to queue!")
        messageQueue.add(data)
        processMessageQueue()
    }

    private fun processMessageQueue() {
        if (messageQueue.isNotEmpty()) {
            try {
                if (binding.stackingMessagesContainer.getChildAt(0) == null) {
                    showMessage(messageQueue.pop())
                }
            } catch (e: NoSuchElementException) {
                Timber.w("Unable to get next available message from queue")
            }
            processAlertBadge()
        } else {
            Timber.v("There are no messages in queue to process")
        }
    }

    private fun showMessage(message: MessageData) {
        Timber.v("Showing message: $message")
        binding.stackingMessagesContainer.apply {
            addView(
                ItemMessageBinding.inflate(
                    layoutInflater,
                    this,
                    false
                ).apply {
                    bind(message.text, resources) { onMessageDismissed() }
                }.root
            )
        }
    }

    private fun onMessageDismissed() {
        binding.stackingMessagesContainer.removeAllViews()
        processMessageQueue()
    }

    private fun processAlertBadge() {
        messageQueue.size().let { size ->
            Timber.v("Processing alert badge with queue size: $size")
            binding.apply {
                when (size) {
                    0 -> stackingMessagesBadge.visibility = View.GONE
                    else -> {
                        stackingMessagesBadge.visibility = View.VISIBLE
                        alertsNo = if (size > 6) "6+" else size.toString()
                    }
                }
            }
        }
    }
}