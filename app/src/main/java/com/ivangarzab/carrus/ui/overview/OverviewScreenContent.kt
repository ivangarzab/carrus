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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.managers.MessageQueue

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
    dueDateFormat: DueDateFormat = DueDateFormat.DAYS,
    sortingType: SortingCallback.SortingType = SortingCallback.SortingType.NONE,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { },
    onServiceEditButtonClicked: (Service) -> Unit = { },
    onServiceDeleteButtonClicked: (Service) -> Unit = { },
    addServiceList: () -> Unit = { },
    onMessageDismissClicked: () -> Unit = { },
    onMessageContentClicked: (String) -> Unit = { }
) {
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
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.combinedClickable(
                            onClick = { },
                            onLongClick = { if (App.isRelease().not()) addServiceList() }
                        ),
                        text = stringResource(id = R.string.services),
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            item {
                OverviewScreenSortingPanel(
                    modifier = Modifier.padding(8.dp),
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
                    OverviewServiceItemStateful(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        index = index,
                        data = serviceList[index],
                        dueDateFormat = dueDateFormat,
                        onEditClicked = onServiceEditButtonClicked,
                        onDeleteClicked = onServiceDeleteButtonClicked
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
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add new car button"
                )
                Text(
                    text = stringResource(id = R.string.add_new_car_capitalized),
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Italic
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
        Box(modifier = modifier
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