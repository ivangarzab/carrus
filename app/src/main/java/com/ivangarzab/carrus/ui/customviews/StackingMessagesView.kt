package com.ivangarzab.carrus.ui.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
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
        if (messageQueue.size() == 0 && isContainerEmpty()) {
            // Only expand if the queue is empty and there's nothing in the container
            expandView()
        }
        messageQueue.add(data)
        processMessageQueue()
    }

    private fun processMessageQueue() {
        if (messageQueue.isNotEmpty()) {
            try {
                if (isContainerEmpty()) {
                    showMessage(messageQueue.pop())
                }
            } catch (e: NoSuchElementException) {
                Timber.w("Unable to get next available message from queue")
            }
            processAlertBadge()
        } else {
            Timber.v("There are no more messages in the queue")
            dismissView()
        }
    }

    private fun isContainerEmpty(): Boolean =
        binding.stackingMessagesContainer.getChildAt(0) == null

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

    private fun expandView() {
        animateViewHeight(expand = true)
    }

    private fun dismissView() {
        animateViewHeight(expand = false)
    }

    private fun animateViewHeight(expand: Boolean) {
        val startValue = when (expand) {
            true -> ANIM_VIEW_HEIGHT_NULL_STATE
            false -> binding.stackingMessagesRoot.measuredHeight
        }.toInt()
        val endValue = when (expand) {
            true -> TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 115f, resources.displayMetrics).toInt()
            false -> ANIM_VIEW_HEIGHT_NULL_STATE
        }.toInt()

        ValueAnimator.ofInt(startValue, endValue).apply {
            duration = ANIM_VIEW_HEIGHT_DURATION_MS
            addUpdateListener { valueAnimator ->
                with(binding.stackingMessagesRoot) {
                    (valueAnimator.animatedValue as Int).let { value ->
                        layoutParams = layoutParams.apply {
                            height = value
                        }
                    }
                }
            }
        }.start()
    }

    companion object {
        private const val ANIM_VIEW_HEIGHT_DURATION_MS: Long = 300
        private const val ANIM_VIEW_HEIGHT_NULL_STATE: Long = -100
    }
}