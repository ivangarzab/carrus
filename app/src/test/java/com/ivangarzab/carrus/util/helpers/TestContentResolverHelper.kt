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

    override fun writeInFile(uri: Uri, content: String): Boolean {
        return uri != Uri.EMPTY && content.isNotBlank()
    }

    override fun readFromFile(uri: Uri): String? {
        return when (uri != Uri.EMPTY) {
            true -> "success"
            false -> null
        }
    }
}