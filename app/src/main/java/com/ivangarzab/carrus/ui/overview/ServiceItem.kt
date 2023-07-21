package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.isPastDue
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */

@Composable
fun OverviewServiceItemStateful(
    modifier: Modifier = Modifier,
    index: Int = -1,
    data: Service = Service.empty,
    dueDateFormat: DueDateFormat,
    onEditClicked: (Service) -> Unit = { },
    onDeleteClicked: (Service) -> Unit = { }
) {
    var isExpanded: Boolean by rememberSaveable {
        mutableStateOf(value = false)
    }

    AppTheme {
        OverviewServiceItem(
            modifier = modifier,
            index = index,
            data = data,
            dueDateFormat = dueDateFormat,
            isExpanded = isExpanded,
            onEditClicked = { onEditClicked(it) },
            onDeleteClicked = { onDeleteClicked(it) }
        ) {
            isExpanded = isExpanded.not()
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewServiceItem(
    modifier: Modifier = Modifier,
    index: Int = -1,
    data: Service = Service.empty,
    dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    isExpanded: Boolean = true,
    onEditClicked: (Service) -> Unit = { },
    onDeleteClicked: (Service) -> Unit = { },
    onExpandClicked: () -> Unit = { }
) {
    AppTheme {
        Card(
            modifier = modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .clickable { onExpandClicked() },
        ) {
            Column(modifier = modifier
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .weight(7f)
                            .align(Alignment.CenterVertically),
                        text = data.name,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .align(Alignment.CenterVertically),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            text = when (data.isPastDue()) {
                                true -> stringResource(id = R.string.due).uppercase(Locale.ROOT)
                                false -> (data.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                                    TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                                        when (daysLeft) {
                                            0L -> stringResource(R.string.tomorrow)
                                            else -> when (dueDateFormat) {
                                                DueDateFormat.DATE -> data.dueDate.getShortenedDate()
                                                DueDateFormat.WEEKS -> stringResource(
                                                    R.string.service_due_date_week_format,
                                                    String.format(
                                                        "%.1f",
                                                        daysLeft / MULTIPLIER_DAYS_TO_WEEKS
                                                    )
                                                )

                                                DueDateFormat.MONTHS -> stringResource(
                                                    R.string.service_due_date_months_format,
                                                    String.format(
                                                        "%.2f",
                                                        daysLeft / MULTIPLIER_DAYS_TO_MONTHS
                                                    )
                                                )

                                                else -> "$daysLeft ${stringResource(R.string.days).lowercase()}"
                                            }
                                        }
                                    } //TODO: Set up logic in VM
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontStyle = when (data.isPastDue()) {
                                true -> FontStyle.Italic
                                false -> null
                            },
                            fontWeight = when (data.isPastDue()) {
                                true -> FontWeight.Bold
                                false -> null
                            },
                            color = when (data.isPastDue()) {
                                true -> MaterialTheme.colorScheme.error
                                false -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = onExpandClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown arrow icon"
                        )
                    }
                }

                if (isExpanded) {
                    Row {
                        Text(
                            modifier = Modifier.weight(7f),
                            text = "${data.brand} - ${data.type}", //TODO: Format this dat in VM
                            style = MaterialTheme.typography.bodyLarge
                        )
                        val costAndDateTextStyle: TextStyle = MaterialTheme.typography.bodyMedium
                        Column(modifier = Modifier.weight(3f)) {
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = data.cost.toString(),
                                style = costAndDateTextStyle
                            )
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = "on ${data.repairDate.getShortenedDate()}",
                                style = costAndDateTextStyle
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = { onEditClicked(data) }
                        ) {
                            Icon(
                                modifier = Modifier.padding(6.dp),
                                painter = painterResource(
                                    id = R.drawable.ic_edit
                                ),
                                contentDescription = "Edit icon button"
                            )
                        }
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onDeleteClicked(data) }
                        ) {
                            Icon(
                                modifier = Modifier.padding(6.dp),
                                painter = painterResource(
                                    id = R.drawable.ic_delete_trash
                                ),
                                contentDescription = "Edit icon button"
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f