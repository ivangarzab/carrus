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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
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
    onSettingsButtonClicked: () -> Unit,
    onServiceEditButtonClicked: (Service) -> Unit,
    onServiceDeleteButtonClicked: (Service) -> Unit,
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
            onAddCarClicked = onAddCarClicked,
            onSortRequest = { viewModel.onSort(it) }
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
    onAddCarClicked: () -> Unit = { },
    onSortRequest: (SortingCallback.SortingType) -> Unit = { }
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
                        serviceList = state.car.services,
                        sortingType = state.serviceSortingType,
                        onSortRequest = onSortRequest
                    )
                },
                bottomBar = {
                    OverviewScreenBottomBar(
                        actionButtonClicked = onFloatingActionButtonClicked,
                        settingsButtonClicked = onSettingsButtonClicked,
                        carEditButtonClicked = onEditButtonClicked,
                        carDetailsButtonClicked = onDetailsButtonClicked
                    )
                }
            )
        } ?: OverviewScreenEmpty(onAddCarClicked = onAddCarClicked)
    }
}