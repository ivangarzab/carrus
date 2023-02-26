package com.ivangarzab.carrus.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.ivangarzab.carrus.databinding.ItemMessageBinding

/**
 * Created by Ivan Garza Bermea.
 */
fun ItemMessageBinding.bind(
    message: String,
    onCloseClickListener: View.OnClickListener? = null
) {
    this.message = message
    this.setCloseButtonClickListener {view ->
        root.animate()
            .alpha(0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onCloseClickListener?.onClick(view)
                }
            })
    }
}