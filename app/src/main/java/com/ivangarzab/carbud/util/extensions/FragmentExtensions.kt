package com.ivangarzab.carbud.util.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by Ivan Garza Bermea.
 */

fun Fragment.toast(message: String) = Toast.makeText(
    requireContext(),
    message,
    Toast.LENGTH_SHORT
).show()

fun Fragment.dismissKeyboard(view: View) =
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }