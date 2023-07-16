package com.ivangarzab.carrus.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Resources
import android.view.MotionEvent
import android.view.View
import com.ivangarzab.carrus.data.MessageData
import com.ivangarzab.carrus.databinding.ItemMessageBinding
import timber.log.Timber
import java.lang.Float.max
import java.lang.Float.min

/**
 * Created by Ivan Garza Bermea.
 */
fun ItemMessageBinding.bind(
    message: MessageData,
    resources: Resources,
    onClickListener: ((id: String) -> Unit)? = null,
    onCloseClickListener: ((id: String) -> Unit)? = null
) {
    this.message = message.text
    this.setCloseButtonClickListener {_ ->
        root.animate()
            .alpha(0f)
            .setDuration(ITEM_MESSAGE_ANIM_DISMISS_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onCloseClickListener?.let { it(message.id) }
                }
            })
    }

    root.setOnClickListener {
        onClickListener?.let { it(message.id) }
    }

    /*root.setOnTouchListener { v, event ->
        // variables to store current configuration of quote card.
        val displayMetrics = resources.displayMetrics
        val cardWidth = root.width
        val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)

        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val currentX = root.x
                Timber.d("MotionEvent ACTION_UP/CANCEL captured with currentX=$currentX")
                // Check if we swiped past the threshold to the left
                if (currentX < MIN_DISTANCE_SWIPE_LEFT || currentX > MIN_DISTANCE_SWIPE_RIGHT) {
                    root.animate()
                        .alpha(0f)
                        .setDuration(ITEM_MESSAGE_ANIM_DISMISS_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                onCloseClickListener?.let { it(message.id) }
                            }
                        })
                        .start()
                } else {
                    root.animate()
                        .x(0f)
                        .setDuration(ITEM_MESSAGE_ANIM_BOUNCE_BACK_DURATION)
                        .start()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = event.rawX
                if (event.historySize > 0) {
                    Timber.v("getX=${event.x} || historicalX=${event.getHistoricalX(0)}")
                    if (event.x < event.getHistoricalX(0)) {
                            root.animate()
                                .x(min(cardStart, newX - (cardWidth / 2)))
                                .setDuration(0)
                                .start()
                    } else if (event.x > event.getHistoricalX(0)) {
                            root.animate()
                                .x(max(newX - (cardWidth / 2), cardStart))
                                .setDuration(0)
                                .start()
                    } else Timber.wtf("It's a Mexican standoff!")
                }
            }
        }
        v.performClick()
        true
    }*/
}
private const val ITEM_MESSAGE_ANIM_DISMISS_DURATION: Long = 300
private const val ITEM_MESSAGE_ANIM_BOUNCE_BACK_DURATION: Long = 150
private const val MIN_DISTANCE_SWIPE_LEFT: Long = -275
private const val MIN_DISTANCE_SWIPE_RIGHT: Long = 275