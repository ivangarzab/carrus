package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.compose.theme.Typography
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CalendarDialog(
    title: String = "Dialog Title",
    date: Calendar = Calendar.getInstance(),
    onDismissed: () -> Unit = { },
    onValueSelected: (Calendar) -> Unit = { },
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.timeInMillis,
    )

    AppTheme {
        BaseDialog(
            isLarge = true,
            onDismissed = onDismissed
        ) {
            Text(
                modifier = Modifier
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            //TODO: Debug check
                            onValueSelected(
                                Calendar.getInstance().apply {
                                    add(Calendar.MONTH, 1)
                                }
                            )
                            onDismissed()
                        }
                    ),
                text = title,
                style = Typography.titleMediumLarge
            )
            DatePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTagsAsResourceId = true }
                    .testTag("Date Picker")
                    .combinedClickable(
                        onClick = {
                            //TODO: Debug check
                            onValueSelected(
                                Calendar
                                    .getInstance()
                                    .apply {
                                        add(Calendar.MONTH, -1)
                                    }
                            )
                            onDismissed()
                        },
                        onLongClick = {
                            //TODO: Debug check
                            onValueSelected(
                                Calendar
                                    .getInstance()
                                    .apply {
                                        add(Calendar.MONTH, -1)
                                        add(Calendar.DAY_OF_MONTH, 1)
                                    }
                            )
                            onDismissed()
                        }
                    ),
                state = datePickerState
            )
            PositiveButton(
                text = stringResource(id = R.string.submit),
                onClick = {
                    datePickerState.selectedDateMillis
                    onValueSelected(
                        Calendar.getInstance().apply {
                            clear()
                            // Set UTC date from picker
                            datePickerState.selectedDateMillis?.let {
                                //TODO: Grab the User's time zone for accuracy!!
                                // -- this mofo of a change fixes a bug.
                                Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                    .apply { timeInMillis = it }
                                    .let { selectedDate ->
                                        set(
                                            selectedDate.get(Calendar.YEAR),
                                            selectedDate.get(Calendar.MONTH),
                                            selectedDate.get(Calendar.DAY_OF_MONTH)
                                        )
                                    }
                            }
                        }
                    )
                    onDismissed()
                }
            )
            NegativeButton(
                text = stringResource(id = R.string.cancel),
                onClick = onDismissed
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    return formatter.format(Date(millis))
}