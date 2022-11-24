package com.ivangarzab.carrus.util.extensions

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
    bottom: Int? =  null
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        top?.let { topMargin = top }
        left?.let { leftMargin = left }
        right?.let { rightMargin = right }
        bottom?.let { bottomMargin = bottom }
    }
}