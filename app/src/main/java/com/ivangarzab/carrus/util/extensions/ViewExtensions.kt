package com.ivangarzab.carrus.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

/**
 * Created by Ivan Garza Bermea.
 */
fun View.updateMargins(
    top: Int? = null,
    left: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        top?.let { topMargin = top }
        left?.let { leftMargin = left }
        right?.let { rightMargin = right }
        bottom?.let { bottomMargin = bottom }
    }
}

fun View.fadeIn(duration: Long, onFinish: () -> Unit) {
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onFinish()
            }
        })
}

fun View.fadeOut(duration: Long, onFinish: () -> Unit) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onFinish()
            }
        })
}