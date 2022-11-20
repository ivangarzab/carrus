package com.ivangarzab.carbud.util.extensions

import android.content.ContentResolver
import android.net.Uri
import timber.log.Timber
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Created by Ivan Garza Bermea.
 */

fun Uri.writeInFile(contentResolver: ContentResolver, content: String) {
    val outputStream: OutputStream
    try {
        outputStream = contentResolver.openOutputStream(this)!!
        val bw = BufferedWriter(OutputStreamWriter(outputStream))
        bw.write(content)
        bw.flush()
        bw.close()
        Timber.d("Successful write to file with uri: $this")
    } catch (e: IOException) {
        Timber.w("Unable to write to file with uri: $this", e)
    }
}