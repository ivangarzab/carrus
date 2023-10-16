package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.structures.MessageQueue
import com.ivangarzab.carrus.ui.compose.PanelIcon
import com.ivangarzab.carrus.ui.compose.PanelTitleText
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenContent(
    modifier: Modifier = Modifier,
    messageQueue: MessageQueue = MessageQueue.test,
    serviceList: List<Service> = Service.serviceList,
    detailsState: DetailsPanelState = DetailsPanelState(),
    dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    sortingType: SortingCallback.SortingType = SortingCallback.SortingType.NONE,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { },
    onServiceEditButtonClicked: (Service) -> Unit = { },
    onServiceDeleteButtonClicked: (Service) -> Unit = { },
    addServiceList: () -> Unit = { },
    onMessageDismissClicked: () -> Unit = { },
    onMessageContentClicked: (String) -> Unit = { }
) {
    var expandedItemIndex: Int by rememberSaveable {
        mutableStateOf(NO_ITEM_EXPANDED)
    }
    var isSortingPanelVisible: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    AppTheme {
        LazyColumn(modifier = modifier) {
            item {
                StackingMessagesPanel(
                    modifier = Modifier
                        .padding(8.dp),
                    messageQueue = messageQueue,
                    onDismissClicked = onMessageDismissClicked,
                    onMessageClicked = { onMessageContentClicked(it) }
                )
            }
            item {
                OverviewScreenDetailsPanel(
                    modifier = Modifier,
                    gridSize = 200.dp,
                    state = detailsState
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                ) {
                    PanelTitleText(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .combinedClickable(
                                onClick = { },
                                onLongClick = {
                                    if (App
                                            .isRelease()
                                            .not()
                                    ) {
                                        // Easter egg for testing!
                                        addServiceList()
                                    }
                                }
                            ),
                        text = stringResource(id = R.string.services)
                    )
                    PanelIcon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { isSortingPanelVisible = isSortingPanelVisible.not() },
                        painter = painterResource(id = R.drawable.ic_sort),
                        contentDescription = "Service sort icon button"
                    )
                }
            }
            item {
                OverviewScreenSortingPanel(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    isVisible = isSortingPanelVisible,
                    onSortRequest = onSortRequest,
                    selectedIndex = when (sortingType) { //TODO: Move into VM's new uiState
                        SortingCallback.SortingType.NONE -> 0
                        SortingCallback.SortingType.NAME -> 1
                        SortingCallback.SortingType.DATE -> 2
                    }
                )
            }
            if (serviceList.isNotEmpty()) {
                itemsIndexed(serviceList) { index, _ ->
                    OverviewServiceItem(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        index = index,
                        data = serviceList[index],
                        dueDateFormat = dueDateFormat,
                        isExpanded = index == expandedItemIndex,
                        onEditClicked = onServiceEditButtonClicked,
                        onDeleteClicked = onServiceDeleteButtonClicked,
                        onExpandOrShrinkRequest = { index, expand ->
                            expandedItemIndex = if (expand) {
                                index
                            } else {
                                NO_ITEM_EXPANDED
                            }
                        }
                    )
                }
            } else {
                item {
                    EmptyListView()
                }
            }
        }
    }
}

private const val NO_ITEM_EXPANDED = -1

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenEmpty(
    onAddCarClicked: () -> Unit = { }
) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onAddCarClicked() }
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                val contentColor = MaterialTheme.colorScheme.onSurface
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_add),
                    tint = contentColor,
                    contentDescription = "Add new car button"
                )
                Text(
                    text = stringResource(id = R.string.add_new_car_capitalized),
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Italic,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun EmptyListView(
    modifier: Modifier = Modifier
) {
    AppTheme {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.no_services_scheduled),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        }
    }
}