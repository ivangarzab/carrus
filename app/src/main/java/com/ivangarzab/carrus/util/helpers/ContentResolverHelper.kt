package com.ivangarzab.carrus.util.helpers

import android.net.Uri

/**
 * Created by Ivan Garza Bermea.
 */
interface ContentResolverHelper {
    fun persistUriPermission(uri: String): Boolean
    fun persistUriPermission(uri: Uri): Boolean
    fun writeInFile(uri: Uri, content: String): Boolean
    fun readFromFile(uri: Uri): String?
}