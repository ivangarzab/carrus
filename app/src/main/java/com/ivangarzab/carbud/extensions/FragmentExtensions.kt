package com.ivangarzab.carbud.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.data.Service

/**
 * Created by Ivan Garza Bermea.
 */

fun Fragment.toast(message: String) = Toast.makeText(
    requireContext(),
    message,
    Toast.LENGTH_SHORT
).show()

fun Fragment.showBottomSheet(onReturn: (Service) -> Unit) =
    (requireActivity() as MainActivity).showBottomSheet { onReturn(it) }

fun Fragment.hideBottomSheet() = (requireActivity() as MainActivity).hideBottomSheet()

fun Fragment.dismissKeyboard(view: View) =
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }