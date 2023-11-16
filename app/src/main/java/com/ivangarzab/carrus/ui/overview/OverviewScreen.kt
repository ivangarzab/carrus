package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.structures.MessageQueue
import com.ivangarzab.carrus.ui.compose.NavigationBottomBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.ui.overview.data.OverviewStatePreviewProvider
import com.ivangarzab.carrus.ui.overview.data.SortingType

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenStateful(
    viewModel: OverviewViewModel = viewModel(),
    onFloatingActionButtonClicked: () -> Unit,
    onCarEditButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onMapButtonClicked: () -> Unit,
    onServiceEditButtonClicked: (Service) -> Unit,
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
            messageQueue = queueState.messageQueue,
            onFloatingActionButtonClicked = onFloatingActionButtonClicked,
            onEditCarButtonClicked = onCarEditButtonClicked,
            onSettingsButtonClicked = onSettingsButtonClicked,
            onMapButtonClicked = onMapButtonClicked,
            onAddCarClicked = onAddCarClicked,
            onSortRequest = { viewModel.onSort(it) },
            onServiceEditButtonClicked = onServiceEditButtonClicked,
            onServiceDeleteButtonClicked = { viewModel.onServiceDeleted(it) },
            onMessageContentClicked = { viewModel.onMessageClicked(it) },
            onMessageDismissClicked = { viewModel.onMessageDismissed() },
            //Easter eggs for testing
            addServiceList = { viewModel.setupEasterEggForTesting() },
            addTestMessage = { viewModel.addTestMessage() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewScreen(
    @PreviewParameter(OverviewStatePreviewProvider::class) state: OverviewState,
    messageQueue: MessageQueue = MessageQueue.test,
    onFloatingActionButtonClicked: () -> Unit = { },
    onEditCarButtonClicked: () -> Unit = { },
    onSettingsButtonClicked: () -> Unit = { },
    onMapButtonClicked: () -> Unit = { },
    onAddCarClicked: () -> Unit = { },
    onSortRequest: (SortingType) -> Unit = { },
    onServiceEditButtonClicked: (Service) -> Unit = { },
    onServiceDeleteButtonClicked: (Service) -> Unit = { },
    onMessageDismissClicked: () -> Unit = { },
    onMessageContentClicked: (String) -> Unit = { },
    addTestMessage: () -> Unit = { },
    addServiceList: () -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val systemUiController = rememberSystemUiController()

    var showServiceModal: Boolean by rememberSaveable {
        mutableStateOf(false)
    } //TODO: Start using this variables instead of the BottomSheetDialogFragment

    AppTheme {
        if (state.car != null) {
            state.car.let {
                systemUiController.statusBarDarkContentEnabled = false
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        OverviewScreenTopBar(
                            title = it.getCarName(),
                            imageUri = it.imageUri,
                            scrollBehavior = scrollBehavior,
                            addTestMessage = addTestMessage
                        )
                    },
                    content = { paddingValues ->
                        OverviewScreenContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            messageQueue = messageQueue,
                            serviceList = it.services,
                            detailsState = DetailsPanelState.fromCar(state.car),
                            dueDateFormat = state.dueDateFormat,
                            sortingType = state.serviceSortingType,
                            onSortRequest = onSortRequest,
                            onEditCarClicked = onEditCarButtonClicked,
                            onServiceEditButtonClicked = onServiceEditButtonClicked,
                            onServiceDeleteButtonClicked = onServiceDeleteButtonClicked,
                            addServiceList = addServiceList,
                            onMessageContentClicked = { onMessageContentClicked(it) },
                            onMessageDismissClicked = onMessageDismissClicked
                        )
                    },
                    bottomBar = {
                        NavigationBottomBar(
                            homeButtonClicked = { },
                            mapButtonClicked = onMapButtonClicked,
                            settingsButtonClicked = onSettingsButtonClicked
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            },
                            onClick = onFloatingActionButtonClicked
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Add Service"
                            )
                        }
                    }
                )
                // Dialog
                when {
                    showServiceModal -> ServiceBottomSheet(
                        modifier = Modifier,
                        onDismissed = { showServiceModal = false }
                    ) //TODO: Start using this!
                }
            }
        } else {
//            systemUiController.statusBarDarkContentEnabled = true
            OverviewScreenEmpty(onAddCarClicked = onAddCarClicked)
        }
    }
}