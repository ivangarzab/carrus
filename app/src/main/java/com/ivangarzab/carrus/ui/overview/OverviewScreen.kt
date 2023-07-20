package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.ui.overview.data.OverviewStatePreviewProvider

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenStateful(
    viewModel: OverviewViewModel = viewModel(),
    onFloatingActionButtonClicked: () -> Unit,
    onCarEditButtonClicked: () -> Unit,
    onCarDetailsButtonClicked: () -> Unit,
    onServiceEditButtonClicked: (Service) -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onAddCarClicked: () -> Unit
) {
    val state: OverviewState by viewModel
        .state
        .observeAsState(initial = OverviewState())

    val queueState: MessageQueueState by viewModel
        .queueState
        .observeAsState(initial = MessageQueueState())

    AppTheme {
        OverviewScreen(
            state = state,
            onFloatingActionButtonClicked = onFloatingActionButtonClicked,
            onEditButtonClicked = onCarEditButtonClicked,
            onDetailsButtonClicked = onCarDetailsButtonClicked,
            onSettingsButtonClicked = onSettingsButtonClicked,
            onAddCarClicked = onAddCarClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewScreen(
    @PreviewParameter(OverviewStatePreviewProvider::class) state: OverviewState,
    onFloatingActionButtonClicked: () -> Unit = { },
    onEditButtonClicked: () -> Unit = { },
    onDetailsButtonClicked: () -> Unit = { },
    onSettingsButtonClicked: () -> Unit = { },
    onAddCarClicked: () -> Unit = { }
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    AppTheme {
        state.car?.let {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    OverviewScreenTopBar(
                        title = state.car.let {
                            it.nickname.ifBlank {
                                "${it.make} ${it.model}"
                            }
                        },
                        imageUri = state.car.imageUri,
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { paddingValues ->
                    OverviewScreenContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        serviceList = state.car.services
                    )
                },
                bottomBar = {
                    OverviewScreenBottomBar(
                        actionButtonClicked = onFloatingActionButtonClicked
                    )
                }
            )
        } ?: OverviewScreenEmpty(onAddCarClicked = onAddCarClicked)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewScreenContent(
    modifier: Modifier = Modifier,
    serviceList: List<Service> = Service.serviceList,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { }
) {
    AppTheme {
        LazyColumn(modifier = modifier) {
            item {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.services),
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            item {
                Row(modifier = Modifier
                    .padding(bottom = 8.dp),
                ) {
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.NONE )},
                        text = stringResource(id = R.string.none)
                    )
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.NAME )},
                        text = stringResource(id = R.string.name)
                    )
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.DATE )},
                        text = stringResource(id = R.string.date)
                    )
                }
            }
            if (serviceList.isNotEmpty()) {
                itemsIndexed(serviceList) { index, item ->
                    OverviewServiceItemStateful(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        index,
                        serviceList[index],
                        onEditClicked = { },
                        onDeleteClicked = { }
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
private fun OverviewScreenEmpty(
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
private fun EmptyListView(
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

@Composable
private fun SortingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    text: String = "button"
) {
    AppTheme {
        TextButton(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                ),
            onClick = onClick,
        ) {
            Text(
                text = text,
                fontStyle = FontStyle.Italic
            )
        }
    }
}