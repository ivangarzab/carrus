package com.ivangarzab.carrus.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.alarm.AlarmFrequency
import com.ivangarzab.carrus.ui.compose.ConfirmationDialog
import com.ivangarzab.carrus.ui.compose.TopBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import com.ivangarzab.carrus.ui.settings.data.SettingsStatePreview
import com.ivangarzab.carrus.ui.settings.dialogs.PickerDialog

/**
 * Created by Ivan Garza Bermea.
 */

@Composable
fun SettingsScreenStateful(
    viewModel: SettingsViewModel = viewModel(),
    onBackPressed: () -> Unit,
    onImportClicked: () -> Unit,
    onExportClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    val state: SettingsState by viewModel
        .state
        .observeAsState(initial = SettingsState())

    AppTheme {
        SettingsScreen(
            state = state,
            onBackPressed = { onBackPressed() },
            onDarkModeToggle = { viewModel.onDarkModeToggleClicked(it) },
            onAlarmsToggle = { viewModel.onAlarmsToggled(it) },
            onAlarmTimeSelected = { viewModel.onAlarmTimePicked(it) },
            onAlarmFrequencyClicked = { viewModel.onAlarmFrequencyPicked(it) },
            onDueDateFormatSelected = { viewModel.onDueDateFormatPicked(it) },
            onClockTimeFormatClicked = { viewModel.onClockTimeFormatPicked(it) },
            onDeleteCarServicesClicked = { viewModel.onDeleteServicesClicked() },
            onDeleteCarDataClicked = { viewModel.onDeleteCarDataClicked() },
            onImportClicked = { onImportClicked() },
            onExportClicked = { onExportClicked() },
            onPrivacyPolicyClicked = onPrivacyPolicyClicked
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreen(
    @PreviewParameter(SettingsStatePreview::class) state: SettingsState,
    onBackPressed: () -> Unit = { },
    onDarkModeToggle: (Boolean) -> Unit = { },
    onAlarmsToggle: (Boolean) -> Unit = { },
    onAlarmTimeSelected: (String) -> Unit = { },
    onAlarmFrequencyClicked: (AlarmFrequency) -> Unit = { },
    onDueDateFormatSelected: (String) -> Unit = { },
    onClockTimeFormatClicked: (String) -> Unit = { },
    onDeleteCarServicesClicked: () -> Unit = { },
    onDeleteCarDataClicked: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onExportClicked: () -> Unit = { },
    onPrivacyPolicyClicked: () -> Unit = { }
) {
    var showAlarmTimePickerDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showDueDateFormatPickerDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showClockTimeFormatPickerDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteCarDataDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteCarServicesDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var showAlarmFrequencyDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Settings",
                    isNavigationIconEnabled = true,
                    onNavigationIconClicked = { onBackPressed() }
                )
            },
            content = { paddingValues ->
                SettingsScreenContent(
                    state = state,
                    modifier = Modifier.padding(paddingValues),
                    onDarkModeToggle = { onDarkModeToggle(it) },
                    onAlarmsToggle = { onAlarmsToggle(it) },
                    onAlarmTimeClicked = { showAlarmTimePickerDialog = true },
                    onAlarmFrequencyClicked = { showAlarmFrequencyDialog = true },
                    onDueDateFormatClicked = { showDueDateFormatPickerDialog = true },
                    onClockTimeFormatClicked = { showClockTimeFormatPickerDialog = true },
                    onDeleteCarDataClicked = { showDeleteCarDataDialog = true },
                    onDeleteCarServicesClicked = { showDeleteCarServicesDialog = true },
                    onImportClicked = { onImportClicked() },
                    onExportClicked = { onExportClicked() },
                    onPrivacyPolicyClicked = onPrivacyPolicyClicked
                )
            }
        )

        // Dialogs
        when {
            showAlarmTimePickerDialog -> PickerDialog(
                items = state.alarmTimeOptions,
//                visibleItemCount = 6, TODO: Figure out how to dynamically calculate Picker size
                onOptionSelected = {
                    showAlarmTimePickerDialog = false
                    onAlarmTimeSelected(it)
                },
                onDismissed = {
                    showAlarmTimePickerDialog = false
                }
            )

            showDueDateFormatPickerDialog -> PickerDialog(
                items = state.dateFormatOptions,
                onOptionSelected = {
                    showDueDateFormatPickerDialog = false
                    onDueDateFormatSelected(it)
                },
                onDismissed = {
                    showDueDateFormatPickerDialog = false
                }
            )

            showDeleteCarDataDialog -> {
                ConfirmationDialog(
                    onConfirmationResult = {
                        if (it) onDeleteCarDataClicked()
                        showDeleteCarDataDialog = false
                    },
                    text = stringResource(id = R.string.dialog_delete_services_title)
                )
            }

            showDeleteCarServicesDialog -> {
                ConfirmationDialog(
                    onConfirmationResult = {
                        if (it) onDeleteCarServicesClicked()
                        showDeleteCarServicesDialog = false
                    },
                    text = stringResource(id = R.string.dialog_delete_car_title)
                )
            }
            showAlarmFrequencyDialog -> PickerDialog(
                items = state.alarmFrequencyOptions,
                onOptionSelected = {
                    showAlarmFrequencyDialog = false
                    onAlarmFrequencyClicked(AlarmFrequency.get(it))
                },
                onDismissed = {
                    showAlarmFrequencyDialog = false
                }
            )
            showClockTimeFormatPickerDialog -> PickerDialog(
                items = state.timeFormatOptions,
                onOptionSelected = {
                    onClockTimeFormatClicked(it)
                    showClockTimeFormatPickerDialog = false
                },
                onDismissed = {
                    showClockTimeFormatPickerDialog = false
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenBottomBar(
    modifier: Modifier = Modifier,
    versionName: String = "0.0.0-test"
) {
    AppTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = versionName,
                style = TextStyle(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}