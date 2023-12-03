package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.DueDateFormat
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.structures.MessageQueue
import com.ivangarzab.carrus.ui.compose.PanelIcon
import com.ivangarzab.carrus.ui.compose.PanelTitleText
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState
import com.ivangarzab.carrus.ui.overview.data.SortingType
import java.util.Random

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
    sortingType: SortingType = SortingType.NONE,
    onSortRequest: (SortingType) -> Unit = { },
    onEditCarClicked: () -> Unit = { },
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
                    state = detailsState,
                    onEditCarClicked = onEditCarClicked
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
                                onLongClick = addServiceList // Easter egg for testing!
                            ),
                        text = stringResource(id = R.string.services)
                    )

                    val sortingIconRotationDegree: Float by animateFloatAsState(
                        targetValue = if (isSortingPanelVisible) 180f else 0f,
                        label = ""
                    )
                    PanelIcon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .rotate(sortingIconRotationDegree),
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
                        SortingType.NONE -> 0
                        SortingType.NAME -> 1
                        SortingType.DATE -> 2
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
            item {
                RotationalQuotePanel()
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
                    contentDescription = "Add new car"
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
                .padding(top = 24.dp, bottom = 16.dp)
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

@Composable
fun RotationalQuotePanel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        val randomQuote: String by rememberSaveable {
            mutableStateOf(quoteList[Random().nextInt(quoteList.size)])
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = randomQuote,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic
        )
    }
}

private val quoteList: List<String> = listOf(
    "Winning isn't everything, but wanting to win is.",
    "Second place is just the first loser.",
    "If you ain't first, you're last.",
    "I am a precision instrument of speed and aerodynamics.",
    "I hate rusty cars.",
    "Float like a Cadillac, sting like a Beemer.",
    "What’s behind you doesn’t matter.",
    "To finish first, you must first finish.",
    "Auto racing began five minutes after the second car was built.",
    "You win some, you lose some, you wreck some.",
    "I live my life a quarter mile at a time.",
    "The winner ain’t the one with the fastest car, it’s the one who refuses to lose.",
    "The crashes people remember, but drivers remember the near misses.",
    "You don’t expect to be at the top of the mountain the day you start climbing.",
    "If you're in control, you're not going fast enough."
)