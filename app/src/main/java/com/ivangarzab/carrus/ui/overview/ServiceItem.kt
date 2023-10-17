package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.isPastDue
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */

@Composable
fun OverviewServiceItem(
    modifier: Modifier = Modifier,
    index: Int,
    data: Service,
    dueDateFormat: DueDateFormat,
    isExpanded: Boolean = true,
    onEditClicked: (Service) -> Unit,
    onDeleteClicked: (Service) -> Unit,
    onExpandOrShrinkRequest: (index: Int, expand: Boolean) -> Unit
) {
    fun onExpandOrShrinkClicked() = onExpandOrShrinkRequest(index, isExpanded.not())

    AppTheme {
        Card(
            modifier = modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .clip(CardDefaults.shape)
                .clickable { onExpandOrShrinkClicked() },
        ) {
            Column(
                modifier = modifier
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
                            style = MaterialTheme.typography.titleSmall,
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

                    val arrowRotationDegree: Float by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f
                    )
                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .rotate(arrowRotationDegree),
                        onClick = { onExpandOrShrinkClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown arrow icon"
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        Row {
                            Text(
                                modifier = Modifier.weight(7f),
                                text = "${data.brand} - ${data.type}", //TODO: Format this dat in VM
                                style = MaterialTheme.typography.bodyLarge
                            )
                            val costAndDateTextStyle: TextStyle =
                                MaterialTheme.typography.bodyMedium
                            Column(modifier = Modifier.weight(3f)) {
                                Text(
                                    modifier = Modifier.align(Alignment.End),
                                    text = NumberFormat.getCurrencyInstance().format(data.cost),
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
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit icon button"
                                )
                            }
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = { onDeleteClicked(data) }
                            ) {
                                Icon(
                                    modifier = Modifier.padding(6.dp),
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Edit icon button"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewServiceItemPreview() {
    AppTheme {
        OverviewServiceItem(
            modifier = Modifier,
            index = -1,
            data = Service.serviceList[1],
            dueDateFormat = DueDateFormat.WEEKS,
            isExpanded = true,
            onEditClicked = { },
            onDeleteClicked = { },
            onExpandOrShrinkRequest = { _: Int, _: Boolean -> }
        )
    }
}

private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f