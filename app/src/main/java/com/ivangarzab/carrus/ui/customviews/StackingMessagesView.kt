package com.ivangarzab.carrus.ui.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ivangarzab.carrus.data.MessageData
import com.ivangarzab.carrus.databinding.ItemMessageBinding
import com.ivangarzab.carrus.databinding.ViewStackingMessagesBinding
import com.ivangarzab.carrus.ui.overview.OverviewViewModel
import com.ivangarzab.carrus.util.extensions.bind
import com.ivangarzab.carrus.util.extensions.fadeIn
import com.ivangarzab.carrus.util.extensions.fadeOut
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

    private var messageQueue: MessageQueue = MessageQueue()

    private var onClickListener: ((String) -> Unit)? = null
    private var onDismissListener: ((String) -> Unit)? = null

    fun feedData(
        owner: LifecycleOwner,
        dataObservable: LiveData<OverviewViewModel.QueueState>
    ) {
        dataObservable.observe(owner) {
            Timber.d("Got a message queue update!")
            if (it.messageQueue.size() > 0 && isContainerEmpty()) {
                // Only expand if the queue is empty and there's nothing in the container
                expandView()
            }
            this.messageQueue = it.messageQueue
            processMessageQueue()
        }
    }

    fun setOnClickListener(onClick: (id: String) -> Unit) {
        this.onClickListener = onClick
    }

    fun setOnDismissListener(onDismiss: (id: String) -> Unit) {
        this.onDismissListener = onDismiss
    }

    private fun processMessageQueue() {
        if (messageQueue.isNotEmpty()) {
            try {
                if (isContainerEmpty()) {
                    showMessage(messageQueue.get())
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
            addView(getMessageItem(message))
        }
    }

    private fun getMessageItem(
        message: MessageData
    ): View = ItemMessageBinding.inflate(
        layoutInflater,
        this,
        false
    ).apply {
        bind(
            message = message,
            resources = resources,
            onClickListener = onClickListener,
            onCloseClickListener = { onMessageDismissed(it) }
        )
    }.root

    private fun onMessageDismissed(id: String) {
        Timber.v("Dismissing message with id=$id")
        binding.stackingMessagesContainer.removeAllViews()
        onDismissListener?.let { it(id) }
    }

    private fun processAlertBadge() {
        messageQueue.size().let { size ->
            Timber.v("Processing alert badge with queue size: $size")
            with(binding) {
                when (size) {
                    0, 1 -> stackingMessagesBadge.visibility = View.GONE
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
            true -> TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ANIM_VIEW_HEIGHT_EXPECTED,
                resources.displayMetrics
            )
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
        private const val ANIM_VIEW_HEIGHT_EXPECTED: Float = 115f
    }
}