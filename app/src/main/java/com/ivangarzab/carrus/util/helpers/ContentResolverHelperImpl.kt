package com.ivangarzab.carrus.util.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
class ContentResolverHelperImpl@Inject constructor(
    @ApplicationContext context: Context
) : ContentResolverHelper {

    private val weakContext = WeakReference(context)

    override fun persistUriPermission(uri: String): Boolean {
        return uri.takeIf { it.isNotEmpty() }
            ?.let { persistUriPermission(it.toUri()) }
            ?: false
    }

    override fun persistUriPermission(uri: Uri): Boolean {
        return try {
            weakContext
                .get()
                ?.contentResolver
                ?.takePersistableUriPermission(
                    uri,
                    PERMISSION_FLAG
                ) ?: throw Exception()
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val PERMISSION_FLAG: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
}