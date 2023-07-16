package com.ivangarzab.carrus.util.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

/**
 * Created by Ivan Garza Bermea.
 */
fun Dialog.clearBackgroundForRoundedCorners() = window?.setBackgroundDrawable(
    ColorDrawable(Color.TRANSPARENT)
)