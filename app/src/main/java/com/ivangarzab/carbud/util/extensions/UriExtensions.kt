package com.ivangarzab.carbud.util.extensions

import android.content.ContentResolver
import android.net.Uri
import timber.log.Timber
import java.io.*

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
        Timber.v("Successful write to file with uri: $this")
    } catch (e: IOException) {
        Timber.w("Unable to write to file with uri: $this", e)
    }
}

fun Uri.readFromFile(contentResolver: ContentResolver): String? {
    var result = ""
    return try {
        val inputStream = contentResolver.openInputStream(this)!!
        val br = BufferedReader(InputStreamReader(inputStream))
        var strLine: String?
        while (br.readLine().also {
                strLine = it
        } != null) {
            result += strLine
        }
        inputStream.close()
        Timber.v("Successfully read from file with uri: $this")
        result
    } catch (e: IOException) {
        Timber.w("Unable to read from file with uri: $this", e)
        null
    }
}