package com.ivangarzab.carbud.extensions

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