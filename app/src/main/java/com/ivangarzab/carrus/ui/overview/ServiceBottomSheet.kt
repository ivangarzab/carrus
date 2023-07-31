package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.BottomSheet
import com.ivangarzab.carrus.ui.compose.CalendarInputField
import com.ivangarzab.carrus.ui.compose.MoneyInputField
import com.ivangarzab.carrus.ui.compose.PositiveButton
import com.ivangarzab.carrus.ui.compose.TextInputField
import com.ivangarzab.carrus.ui.compose.previews.ServiceModalStatePreviewProvider
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.modals.ServiceModalState
import com.ivangarzab.carrus.ui.modals.ServiceModalViewModel

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun ServiceBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: ServiceModalViewModel = viewModel(),
    onDismissed: () -> Unit = { },
) {
    val state: ServiceModalState by viewModel
        .state
        .observeAsState(initial = ServiceModalState())

    AppTheme {
        BottomSheet(
            modifier = modifier,
            onDismissed = onDismissed
        ) {
            ServiceBottomSheetContent(
                modifier = Modifier,
                state = state
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ServiceBottomSheetContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(ServiceModalStatePreviewProvider::class) state: ServiceModalState,
    onUpdateState: (ServiceModalState) -> Unit = { },
    onRepairDateFieldClicked: () -> Unit = { },
    onDueDateFieldClicked: () -> Unit = { },
    onActionButtonClicked: () -> Unit = { }
) {
    AppTheme {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(id = R.string.services),
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Italic,
                )
                TextInputField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    label = stringResource(id = R.string.service_name),
                    content = state.name ?: "",
                    isRequired = true,
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
                PositiveButton(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(id = R.string.submit),
                    onClick = onActionButtonClicked
                )
            }
        }
    }
}