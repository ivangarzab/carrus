package com.ivangarzab.carrus.ui.modal_service

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.ui.compose.BigPositiveButton
import com.ivangarzab.carrus.ui.compose.BottomSheet
import com.ivangarzab.carrus.ui.compose.CalendarDialog
import com.ivangarzab.carrus.ui.compose.CalendarInputField
import com.ivangarzab.carrus.ui.compose.MoneyInputField
import com.ivangarzab.carrus.ui.compose.TextInputField
import com.ivangarzab.carrus.ui.compose.previews.ServiceModalStatePreviewProvider
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.compose.theme.Typography
import com.ivangarzab.carrus.ui.modal_service.data.ServiceModalState
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
data class ServiceModalInputData(
    val mode: ServiceModalState.Mode = ServiceModalState.Mode.NULL,
    val service: Service? = null
)
@Composable
fun ServiceBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: ServiceModalViewModel = viewModel(),
    inputData: ServiceModalInputData,
    onDismissed: (success: Boolean) -> Unit,
) {
    val context = LocalContext.current

    val state: ServiceModalState by viewModel
        .state
        .observeAsState(initial = ServiceModalState())

    val showRepairDateDialog: Boolean by viewModel
        .isShowingRepairDateDialog
        .observeAsState(initial = false)

    val showDueDateDialog: Boolean by viewModel
        .isShowingDueDateDialog
        .observeAsState(initial = false)

    viewModel.apply {
        // Only update initial state when state.mode == NULL
        if (state.mode == ServiceModalState.Mode.NULL) {
            setInitialData(inputData.service, inputData.mode)
        }
        onSubmission.observe(LocalLifecycleOwner.current) { success ->
            when (success) {
                true -> onDismissed(true)
                false -> Toast.makeText(
                    context,
                    "Missing required field(s) or wrong data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    AppTheme {
        BottomSheet(
            modifier = modifier,
            onDismissed = {
                viewModel.onClearState()
                onDismissed(false)
            }
        ) {
            ServiceBottomSheetContent(
                modifier = modifier,
                state = state,
                onActionButtonClicked = { viewModel.onActionButtonClicked() },
                onUpdateState = { viewModel.onUpdateServiceModalState(it) },
                onRepairDateFieldClicked = { viewModel.onShowRepairDateDialog() },
                onDueDateFieldClicked = { viewModel.onShowDueDateDialog() }
            )
        }
    }

    when {
        showRepairDateDialog -> {
            CalendarDialog(
                title = "Repair Date",
                onDismissed = { viewModel.onHideRepairDateDialog() },
                onValueSelected = {
                    viewModel.onUpdateServiceModalState(
                        state.copy(repairDate = it.getShortenedDate())
                    )
                }
            )
        }

        showDueDateDialog -> {
            CalendarDialog(
                title = "Due Date",
                date = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, ServiceModalViewModel.DEFAULT_DUE_DATE_ADDITION)
                },
                onDismissed = { viewModel.onHideDueDateDialog() },
                onValueSelected = {
                    viewModel.onUpdateServiceModalState(
                        state.copy(dueDate = it.getShortenedDate())
                    )
                }
            )
        }
    }
}

@Composable
fun ServiceBottomSheetContent(
    modifier: Modifier = Modifier,
    state: ServiceModalState,
    onUpdateState: (ServiceModalState) -> Unit,
    onRepairDateFieldClicked: () -> Unit,
    onDueDateFieldClicked: () -> Unit,
    onActionButtonClicked: () -> Unit
) {
    AppTheme {
        Surface(modifier = modifier) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = state.title ?: stringResource(id = R.string.services),
                    style = Typography.titleMediumLarge,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.service_name),
                    content = state.name ?: "",
                    isRequired = true,
                    keyboardAction = KeyboardActions(
                        onNext = { onRepairDateFieldClicked() }
                    ),
                    updateListener = {
                        onUpdateState(
                            state.copy(
                                name = it
                            )
                        )
                    }
                )
                CalendarInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    content = state.repairDate ?: "",
                    label = stringResource(id = R.string.repair_date),
                    isRequired = true,
                    updateListener = { /* No-op */ },
                    onClick = onRepairDateFieldClicked
                )
                CalendarInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    content = state.dueDate ?: "",
                    label = stringResource(id = R.string.due_date),
                    isRequired = true,
                    updateListener = { /* No-op */ },
                    onClick = onDueDateFieldClicked
                )
                TextInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.service_brand),
                    content = state.brand ?: "",
                    updateListener = {
                        onUpdateState(
                            state.copy(
                                brand = it
                            )
                        )
                    }
                )
                TextInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.service_type),
                    content = state.type ?: "",
                    updateListener = {
                        onUpdateState(
                            state.copy(
                                type = it
                            )
                        )
                    }
                )
                MoneyInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.service_price),
                    content = state.price ?: "",
                    isLastField = true,
                    updateListener = {
                        onUpdateState(
                            state.copy(
                                price = it
                            )
                        )
                    }
                )
                BigPositiveButton(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(id = R.string.submit),
                    onClick = onActionButtonClicked
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ServiceBottomSheetContentPreview() {
    ServiceBottomSheetContent(
        state = ServiceModalStatePreviewProvider().values.first(),
        onUpdateState = { },
        onRepairDateFieldClicked = { },
        onDueDateFieldClicked = { },
        onActionButtonClicked = { }
    )
}