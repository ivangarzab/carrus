package com.ivangarzab.carrus.util.helpers

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import timber.log.Timber
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.ref.WeakReference

/**
 * Created by Ivan Garza Bermea.
 */
class ContentResolverHelperImpl(
    context: Context
) : ContentResolverHelper {

    private val weakContext = WeakReference(context)

    private fun getContentResolver(): ContentResolver {
        weakContext.get()?.let {
            return it.contentResolver
        } ?: throw IllegalStateException()
    }

    override fun persistUriPermission(uri: String): Boolean {
        return uri.takeIf { it.isNotEmpty() }
            ?.let { persistUriPermission(it.toUri()) }
            ?: false
    }

    override fun persistUriPermission(uri: Uri): Boolean {
        return try {
            getContentResolver().takePersistableUriPermission(uri, PERMISSION_FLAG)
            true
        } catch (e: Exception) {
            Timber.w("Unable to persist uri permission: $this", e)
            false
        }
    }

    override fun writeInFile(uri: Uri, content: String): Boolean {
        return try {
            getContentResolver().openOutputStream(uri)?.let { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).apply {
                    write(content)
                    flush()
                    close()
                }
                Timber.v("Successful write to file with uri: $this")
                true
            } ?: false
        } catch (e: IOException) {
            Timber.w("Unable to write to file with uri: $this", e)
            false
        }
    }

    override fun readFromFile(uri: Uri): String? {
        var result = ""
        return try {
            getContentResolver().openInputStream(uri).let { inputStream ->
                val br = BufferedReader(InputStreamReader(inputStream))
                var strLine: String?
                while (br.readLine().also {
                        strLine = it
                    } != null) {
                    result += strLine
                }
                inputStream?.close()
                Timber.v("Successfully read from file with uri: $this")
                result
            }
        } catch (e: IOException) {
            Timber.w("Unable to read from file with uri: $this", e)
            null
        }
    }

    companion object {
        private const val PERMISSION_FLAG: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
}