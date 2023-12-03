package com.ivangarzab.carrus.util.extensions

import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

/**
 * Created by Ivan Garza Bermea.
 */

fun Fragment.toast(
    message: String,
    isLong: Boolean = false
) = Toast.makeText(requireContext(), message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()

fun Fragment.dismissKeyboard(view: View) =
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }

@Suppress("DEPRECATION")
fun Fragment.setLightStatusBar(light: Boolean) {
    if (Build.VERSION.SDK_INT < 30) {
        requireActivity().window.decorView.systemUiVisibility = when (light) {
            true -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            false -> 0
        }
    } else {
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            when (light) {
                true -> WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                false -> 0
            },
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}

fun Fragment.onBackPressed(operation: () -> Unit) =
    requireActivity().onBackPressedDispatcher.addCallback(
        this.viewLifecycleOwner
    ) {
        operation()
    }