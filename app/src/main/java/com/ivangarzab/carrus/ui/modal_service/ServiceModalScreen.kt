package com.ivangarzab.carrus.ui.modal_service

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.ui.compose.CalendarDialog
import com.ivangarzab.carrus.ui.compose.previews.ServiceModalStatePreviewProvider
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.modal_service.ServiceModalViewModel.Companion.DEFAULT_DUE_DATE_ADDITION
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.ui.modals.ServiceModalFragmentArgs
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
@Deprecated("No longer in use... for now")
@Composable
fun ServiceModalScreenStateful(
    viewModel: ServiceModalViewModel = viewModel(),
    args: ServiceModalFragmentArgs,
    onSubmissionSuccess: (Boolean) -> Unit,
) {
    val state: ServiceModalState by viewModel
        .state
        .observeAsState(initial = ServiceModalState())

    viewModel.onSubmission.observe(LocalLifecycleOwner.current) { onSubmissionSuccess(it) }

    AppTheme {
        ServiceModalScreen(
            state = state,
            onUpdateState = { viewModel.onUpdateServiceModalState(it) },
            onActionButtonClicked = { viewModel.onActionButtonClicked() }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Deprecated("All this code was moved into ServiceBottomSheet")
@Composable
fun ServiceModalScreen(
    modifier: Modifier = Modifier,
    @PreviewParameter(ServiceModalStatePreviewProvider::class) state: ServiceModalState,
    onUpdateState: (ServiceModalState) -> Unit = { },
    onActionButtonClicked: () -> Unit = { },
) {
    var showRepairDateDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showDueDateDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    AppTheme {
        ServiceBottomSheetContent(
            modifier = modifier,
            state = state,
            onActionButtonClicked = onActionButtonClicked,
            onUpdateState = onUpdateState,
            onRepairDateFieldClicked = { showRepairDateDialog = true },
            onDueDateFieldClicked = { showDueDateDialog = true }
        )

        when {
            showRepairDateDialog -> {
                CalendarDialog(
                    title = "Repair Date",
                    onDismissed = { showRepairDateDialog = false },
                    onValueSelected = {
                        onUpdateState(
                            state.copy(
                                repairDate = it.getShortenedDate()
                            )
                        )
                        // Trigger the next Calendar dialog, as needed
                        if (state.dueDate.isNullOrBlank()) {
                            showDueDateDialog = true
                        }
                    }
                )
            }

            showDueDateDialog -> {
                CalendarDialog(
                    title = "Due Date",
                    date = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_MONTH, DEFAULT_DUE_DATE_ADDITION)
                    },
                    onDismissed = { showDueDateDialog = false },
                    onValueSelected = {
                        onUpdateState(
                            state.copy(
                                dueDate = it.getShortenedDate()
                            )
                        )
                    }
                )
            }
        }
    }
}