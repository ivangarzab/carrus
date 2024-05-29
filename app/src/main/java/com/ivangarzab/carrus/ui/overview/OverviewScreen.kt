package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.data.structures.MessageQueue
import com.ivangarzab.carrus.ui.compose.NavigationBottomBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.modal_service.ServiceBottomSheet
import com.ivangarzab.carrus.ui.modal_service.ServiceModalInputData
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState
import com.ivangarzab.carrus.ui.overview.data.MessageQueueState
import com.ivangarzab.carrus.ui.overview.data.OverviewStaticState
import com.ivangarzab.carrus.ui.overview.data.ServicePanelState
import com.ivangarzab.carrus.ui.overview.data.SortingType

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenStateful(
    viewModel: OverviewViewModel = viewModel(),
    onCarEditButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onMapButtonClicked: () -> Unit,
    onAddCarClicked: () -> Unit
) {
    val staticState: OverviewStaticState by viewModel
        .staticState
        .observeAsState(initial = OverviewStaticState())

    val detailsPanelState: DetailsPanelState by viewModel
        .detailsPanelState
        .observeAsState(initial = DetailsPanelState())

    val servicePanelState: ServicePanelState by viewModel
        .servicePanelState
        .observeAsState(initial = ServicePanelState())

    val serviceModalData: ServiceModalInputData by viewModel
        .serviceModalData
        .observeAsState(initial = ServiceModalInputData())

    val queueState: MessageQueueState by viewModel
        .queueState
        .observeAsState(initial = MessageQueueState())

    AppTheme {
        OverviewScreen(
            staticState = staticState,
            detailsPanelState = detailsPanelState,
            servicePanelState = servicePanelState,
            serviceModalData = serviceModalData,
            messageQueue = queueState.messageQueue,
            onEditCarButtonClicked = onCarEditButtonClicked,
            onSettingsButtonClicked = onSettingsButtonClicked,
            onMapButtonClicked = onMapButtonClicked,
            onAddCarClicked = onAddCarClicked,
            onSortRequest = { viewModel.onSort(it) },
            onServiceCreateButtonClicked = { viewModel.onServiceCreate() },
            onServiceEditButtonClicked = { viewModel.onServiceEdit(it) },
            onServiceRescheduleClicked = { viewModel.onServiceReschedule(it) },
            onServiceCompleteClicked = { viewModel.onServiceCompleted(it) },
            onServiceDeleteButtonClicked = { viewModel.onServiceDeleted(it) },
            onServiceModalDismissed = { viewModel.onServiceModalDismissed() },
            onMessageContentClicked = { viewModel.onMessageClicked(it) },
            onMessageDismissClicked = { viewModel.onMessageDismissed() },
            //Easter eggs for testing
            addServiceList = { viewModel.setupEasterEggForTesting() },
            addTestMessage = { viewModel.addTestMessage() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OverviewScreen(
    staticState: OverviewStaticState,
    detailsPanelState: DetailsPanelState,
    servicePanelState: ServicePanelState,
    serviceModalData: ServiceModalInputData,
    messageQueue: MessageQueue = MessageQueue.test,
    onEditCarButtonClicked: () -> Unit = { },
    onSettingsButtonClicked: () -> Unit = { },
    onMapButtonClicked: () -> Unit = { },
    onAddCarClicked: () -> Unit = { },
    onSortRequest: (SortingType) -> Unit = { },
    onServiceCreateButtonClicked: () -> Unit = { },
    onServiceEditButtonClicked: (Service) -> Unit = { },
    onServiceRescheduleClicked: (Service) -> Unit = { },
    onServiceCompleteClicked: (Service) -> Unit = { },
    onServiceDeleteButtonClicked: (Service) -> Unit = { },
    onServiceModalDismissed: () -> Unit = { },
    onMessageDismissClicked: () -> Unit = { },
    onMessageContentClicked: (String) -> Unit = { },
    addTestMessage: () -> Unit = { },
    addServiceList: () -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val systemUiController = rememberSystemUiController()

    var showServiceScheduledConfirmation: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    var showServiceUpdatedConfirmation: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    AppTheme {
        if (staticState.isDataEmpty.not()) {
            systemUiController.statusBarDarkContentEnabled = false
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    OverviewScreenTopBar(
                        title = staticState.carName,
                        imageUri = staticState.imageUri,
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
                        servicesState = servicePanelState,
                        detailsState = detailsPanelState,
                        onSortRequest = onSortRequest,
                        onEditCarClicked = onEditCarButtonClicked,
                        onServiceCompleteClicked = onServiceCompleteClicked,
                        onServiceEditButtonClicked = onServiceEditButtonClicked,
                        onServiceRescheduleClicked = onServiceRescheduleClicked,
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
                        onClick = onServiceCreateButtonClicked
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
            serviceModalData.let {
                when {
                    it.mode != ServiceModalState.Mode.NULL -> {
                        ServiceBottomSheet(
                            modifier = Modifier,
                            inputData = it,
                            onDismissed = { success ->
                                if (success) {
                                    when (it.mode) {
                                        ServiceModalState.Mode.CREATE -> {
                                            showServiceScheduledConfirmation = true
                                        }
                                        else -> showServiceUpdatedConfirmation = true
                                    }
                                }
                                // dismiss service modal and clear data
                                onServiceModalDismissed()
                            }
                        )
                    }
                }
            }

            // Confirmation scrims
            AnimatedVisibility(
                visible = showServiceScheduledConfirmation,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = TweenSpec(
                        delay = SCHEDULE_ACTION_SCRIM_DURATION_MS
                    )
                )
            ) {
                OverviewServiceScheduledScrim(
                    modifier = Modifier.fillMaxSize(),
                    text = "Service scheduled",
                    onFinishWaiting = { showServiceScheduledConfirmation = false }
                )
            }
            AnimatedVisibility(
                visible = showServiceUpdatedConfirmation,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = TweenSpec(
                        delay = SCHEDULE_ACTION_SCRIM_DURATION_MS
                    )
                )
            ) {
                OverviewServiceScheduledScrim(
                    modifier = Modifier.fillMaxSize(),
                    text = "Service updated",
                    onFinishWaiting = { showServiceUpdatedConfirmation = false }
                )
            }
        } else {
            systemUiController.statusBarDarkContentEnabled = isSystemInDarkTheme().not()
            OverviewScreenEmpty(onAddCarClicked = onAddCarClicked)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewScreenPreview() {
    OverviewScreen(
        staticState = OverviewStaticState(
            isDataEmpty = false,
            carName = "Shaq"
        ),
        detailsPanelState = DetailsPanelState(),
        servicePanelState = ServicePanelState(),
        serviceModalData = ServiceModalInputData(),
        messageQueue = MessageQueue.test,
        onEditCarButtonClicked = { },
        onSettingsButtonClicked = { },
        onMapButtonClicked = { },
        onAddCarClicked = { },
        onSortRequest = { },
        onServiceDeleteButtonClicked = { },
        onMessageDismissClicked = { },
        onMessageContentClicked = { },
        addTestMessage = { },
        addServiceList = { }
    )
}

private const val SCHEDULE_ACTION_SCRIM_DURATION_MS = 450