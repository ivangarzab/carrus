package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.models.Shop
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.ServiceItemState
import java.text.NumberFormat

/**
 * Created by Ivan Garza Bermea.
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OverviewServiceItem(
    modifier: Modifier = Modifier,
    index: Int,
    data: ServiceItemState,
    isExpanded: Boolean = true,
    onEditClicked: (Service) -> Unit,
    onCompleteClicked: (Service) -> Unit,
    onRescheduleClicked: (Service) -> Unit,
    onDeleteClicked: (Service) -> Unit,
    onShopDetailsClicked: (Service) -> Unit,
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
                    .semantics { testTagsAsResourceId = true }
                    .testTag("Service ${data.name} index ${data.index}")
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .weight(7f)
                            .align(Alignment.CenterVertically),
                        text = data.name,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .align(Alignment.CenterVertically),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            text = data.dueDateFormatted,
                            style = when (data.isPastDue) {
                                true -> MaterialTheme.typography.titleMedium
                                false -> MaterialTheme.typography.titleSmall
                            },
                            fontStyle = when (data.isPastDue) {
                                true -> FontStyle.Italic
                                false -> null
                            },
                            fontWeight = when (data.isPastDue) {
                                true -> FontWeight.Bold
                                false -> null
                            },
                            color = when (data.isPastDue) {
                                true -> MaterialTheme.colorScheme.error
                                false -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }

                    val arrowRotationDegree: Float by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        label = ""
                    )
                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .rotate(arrowRotationDegree),
                        onClick = { onExpandOrShrinkClicked() }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_expand),
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
                                text = data.details,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            val costAndDateTextStyle: TextStyle =
                                MaterialTheme.typography.bodyMedium
                            Column(modifier = Modifier.weight(3f)) {
                                Text(
                                    modifier = Modifier.align(Alignment.End),
                                    text = data.price,
                                    style = costAndDateTextStyle
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.End),
                                    text = data.repairDate,
                                    style = costAndDateTextStyle
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        // Only show shop row if there's enough information available
                        data.data.shop?.let { shop: Shop ->
                            OverviewServiceItemShopRow(
                                data = shop,
                                onShopDetailsClicked = { onShopDetailsClicked(data.data) }
                            )
                            Divider(
                                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        OverviewServiceItemActionRow(
                            data = data,
                            onEditClicked = onEditClicked,
                            onCompleteClicked = onCompleteClicked,
                            onRescheduleClicked = onRescheduleClicked,
                            onDeleteClicked = onDeleteClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewServiceItemShopRow(
    modifier: Modifier = Modifier,
    data: Shop,
    onShopDetailsClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onShopDetailsClicked() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = data.name + " at: " + data.address.getShortString(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_navigate),
            contentDescription = "Shop map icon"
        )
    }
}

@Composable
private fun OverviewServiceItemActionRow(
    modifier: Modifier = Modifier,
    data: ServiceItemState,
    onEditClicked: (Service) -> Unit,
    onCompleteClicked: (Service) -> Unit,
    onRescheduleClicked: (Service) -> Unit,
    onDeleteClicked: (Service) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            IconButton(
                onClick = { onDeleteClicked(data.data) }
            ) {
                Icon(
                    modifier = Modifier.padding(6.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_trash),
                    contentDescription = "Delete icon button"
                )
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            IconButton(
                onClick = { onRescheduleClicked(data.data) }
            ) {
                Icon(
                    modifier = Modifier.padding(6.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_repeat),
                    contentDescription = "Reschedule icon button"
                )
            }
            IconButton(
                onClick = { onCompleteClicked(data.data) }
            ) {
                Icon(
                    modifier = Modifier.padding(6.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_checkmark),
                    contentDescription = "Complete icon button"
                )
            }
            IconButton(
                onClick = { onEditClicked(data.data) }
            ) {
                Icon(
                    modifier = Modifier.padding(6.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit icon button"
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewServiceItemPreviewNoShop() {
    AppTheme {
        OverviewServiceItem(
            modifier = Modifier,
            index = -1,
            data = Service.serviceList[2].let {
                ServiceItemState(
                    index = 1,
                    name = it.name,
                    details = "No deets",
                    price = NumberFormat.getCurrencyInstance().format(it.cost),
                    repairDate = "on 9/12/1992",
                    dueDateFormatted = "DUE",
                    isPastDue = true,
                    data = it
                )
            },
            isExpanded = true,
            onEditClicked = { },
            onCompleteClicked = { },
            onRescheduleClicked = { },
            onDeleteClicked = { },
            onExpandOrShrinkRequest = { _: Int, _: Boolean -> },
            onShopDetailsClicked = { }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewServiceItemPreviewWithShop() {
    AppTheme {
        OverviewServiceItem(
            modifier = Modifier,
            index = -1,
            data = Service.serviceList[1].let {
                ServiceItemState(
                    index = 1,
                    name = it.name,
                    details = "No deets",
                    price = NumberFormat.getCurrencyInstance().format(it.cost),
                    repairDate = "on 9/12/1992",
                    dueDateFormatted = "DUE",
                    isPastDue = true,
                    data = it
                )
            },
            isExpanded = true,
            onEditClicked = { },
            onCompleteClicked = { },
            onRescheduleClicked = { },
            onDeleteClicked = { },
            onExpandOrShrinkRequest = { _: Int, _: Boolean -> },
            onShopDetailsClicked = { }
        )
    }
}
