package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.ui.overview.data.OverviewStatePreviewProvider
import com.ivangarzab.carrus.util.managers.MessageQueue

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenStateful(
    viewModel: OverviewViewModel = viewModel(),
    onFloatingActionButtonClicked: () -> Unit,
    onCarEditButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onServiceEditButtonClicked: (Service) -> Unit,
    onAddCarClicked: () -> Unit,
    onMessageClicked: (String) -> Unit
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
            onEditButtonClicked = onCarEditButtonClicked,
            onSettingsButtonClicked = onSettingsButtonClicked,
            onAddCarClicked = onAddCarClicked,
            onSortRequest = { viewModel.onSort(it) },
            onServiceEditButtonClicked = onServiceEditButtonClicked,
            onServiceDeleteButtonClicked = { viewModel.onServiceDeleted(it)},
            onMessageContentClicked = { onMessageClicked(it) },
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
    onEditButtonClicked: () -> Unit = { },
    onSettingsButtonClicked: () -> Unit = { },
    onAddCarClicked: () -> Unit = { },
    onSortRequest: (SortingCallback.SortingType) -> Unit = { },
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

    var showCarDetailsDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

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
                            title = it.nickname.ifBlank {
                                "${it.make} ${it.model}"
                            },
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
                            dueDateFormat = state.dueDateFormat,
                            sortingType = state.serviceSortingType,
                            onSortRequest = onSortRequest,
                            onServiceEditButtonClicked = onServiceEditButtonClicked,
                            onServiceDeleteButtonClicked = onServiceDeleteButtonClicked,
                            addServiceList = addServiceList,
                            onMessageContentClicked = { onMessageContentClicked(it) },
                            onMessageDismissClicked = onMessageDismissClicked
                        )
                    },
                    bottomBar = {
                        OverviewScreenBottomBar(
                            actionButtonClicked = onFloatingActionButtonClicked,
                            settingsButtonClicked = onSettingsButtonClicked,
                            carEditButtonClicked = onEditButtonClicked,
                            carDetailsButtonClicked = {
                                showCarDetailsDialog = true
                            }
                        )
                    }
                )
                // Dialog
                when {
                    showCarDetailsDialog -> CarDetailsDialog(
                        vinNo = it.vinNo,
                        tirePressure = it.tirePressure,
                        milesTotal = it.totalMiles,
                        milesPerGallon = it.milesPerGallon,
                        onClick = {
                            showCarDetailsDialog = false
                        }
                    )
                    showServiceModal -> ServiceBottomSheet(
                        modifier = Modifier,
                        onDismissed = { showServiceModal = false }
                    ) //TODO: Start using this!
                }
            }
        } else {
            systemUiController.statusBarDarkContentEnabled = true
            OverviewScreenEmpty(onAddCarClicked = onAddCarClicked)
        }
    }
}