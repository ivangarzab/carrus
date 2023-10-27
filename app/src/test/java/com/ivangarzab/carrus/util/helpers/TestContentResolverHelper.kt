package com.ivangarzab.carrus.util.helpers

import android.net.Uri

/**
 * Created by Ivan Garza Bermea.
 */
class TestContentResolverHelper : ContentResolverHelper {

    override fun persistUriPermission(uri: String): Boolean {
        return uri.takeIf { it.isNotEmpty() }
            ?.let { true }
            ?: false
    }

    override fun persistUriPermission(uri: Uri): Boolean {
        return uri != Uri.EMPTY
    }
}