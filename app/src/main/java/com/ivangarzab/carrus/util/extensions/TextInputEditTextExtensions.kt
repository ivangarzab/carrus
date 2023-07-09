package com.ivangarzab.carrus.util.extensions

import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by Ivan Garza Bermea.
 */

/**
 * The purpose of this extension function is to take the contents of a [TextInputEditText]
 * component, and convert it into a [Calendar] date, when applicable.  If the contents turn out
 *  to be blank, then a [Calendar] will be returned where [Calendar.getTimeInMillis] == 0.
 */
fun TextInputEditText.getCalendarDate(): Calendar = text.toString().let { content ->
    when (content.isNotBlank()) {
        true -> Calendar.getInstance().apply {
            time = SimpleDateFormat("MM/dd/yy", Locale.US)
                .parse(content) as Date
        }
        false -> Calendar.getInstance().apply { timeInMillis = 0L }
    }
}
