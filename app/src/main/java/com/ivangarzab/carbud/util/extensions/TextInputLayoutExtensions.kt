package com.ivangarzab.carbud.util.extensions

import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Ivan Garza Bermea.
 */
fun TextInputLayout.markRequired() {
    hint = "$hint*"
}