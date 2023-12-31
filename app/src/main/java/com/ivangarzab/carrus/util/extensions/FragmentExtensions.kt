package com.ivangarzab.carrus.util.extensions

import android.content.Context
import android.view.View
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

fun Fragment.onBackPressed(operation: () -> Unit) =
    requireActivity().onBackPressedDispatcher.addCallback(
        this.viewLifecycleOwner
    ) {
        operation()
    }