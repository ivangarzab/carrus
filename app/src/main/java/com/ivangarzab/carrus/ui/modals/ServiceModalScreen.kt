package com.ivangarzab.carrus.ui.modals

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.BaseDialog
import com.ivangarzab.carrus.ui.compose.NegativeButton
import com.ivangarzab.carrus.ui.compose.PositiveButton
import com.ivangarzab.carrus.ui.compose.previews.ServiceModalStatePreviewProvider
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.modals.ServiceModalViewModel.Companion.DEFAULT_DUE_DATE_ADDITION
import com.ivangarzab.carrus.ui.overview.ServiceBottomSheetContent
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun ServiceModalScreenStateful(
    viewModel: ServiceModalViewModel = viewModel(),
    args: ServiceModalFragmentArgs,
    onSubmissionSuccess: (Boolean) -> Unit,
) {
    viewModel.setArgsData(args.service)
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
                    onDismissed = { showRepairDateDialog = false },
                    onValueSelected = {
                        onUpdateState(
                            state.copy(
                                repairDate = it.getShortenedDate()
                            )
                        )
                    }
                )
            }

            showDueDateDialog -> {
                CalendarDialog(
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CalendarDialog(
    date: Calendar = Calendar.getInstance(),
    onDismissed: () -> Unit = { },
    onValueSelected: (Calendar) -> Unit = { },
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.timeInMillis,
    )

    AppTheme {
        BaseDialog(
            isLarge = true,
            onDismissed = onDismissed
        ) {
            DatePicker(
                modifier = Modifier.fillMaxWidth(),
                state = datePickerState
            )
            PositiveButton(
                text = stringResource(id = R.string.submit),
                onClick = {
                    datePickerState.selectedDateMillis
                    onValueSelected(
                        Calendar.getInstance().apply {
                            clear()
                            // Set UTC date from picker
                            datePickerState.selectedDateMillis?.let {
                                Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                    .apply { timeInMillis = it }
                                    .let { selectedDate ->
                                        set(
                                            selectedDate.get(Calendar.YEAR),
                                            selectedDate.get(Calendar.MONTH),
                                            selectedDate.get(Calendar.DAY_OF_MONTH)
                                        )
                                    }
                            }
                        }
                    )
                    onDismissed()
                }
            )
            NegativeButton(
                text = stringResource(id = R.string.cancel),
                onClick = onDismissed
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    return formatter.format(Date(millis))
}