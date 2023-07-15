package com.ivangarzab.carrus.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(SettingsStatePreview::class) state: SettingsViewModel.SettingsState,
    onDarkModeToggle: (Boolean) -> Unit = { },
    onAlarmTimeClicked: () -> Unit = { },
    onDueDateFormatClicked: () -> Unit = { },
    onDeleteCarServicesClicked: () -> Unit = { },
    onDeleteCarDataClicked: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onExportClicked: () -> Unit = { }
) {
    AppTheme {
        LazyColumn(
            modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            item { // Dark Mode Toggle
                SettingsScreenContentItemSwitch(
                    title = stringResource(id = R.string.setting_dark_mode_title),
                    subTitle = stringResource(id = R.string.setting_dark_mode_subtitle),
                    isChecked = isSystemInDarkTheme(),
                    onToggle = { onDarkModeToggle(it) }
                )
            }

            item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
            item { // Alarm Time
                SettingsScreenContentItemText(
                    title = stringResource(id = R.string.setting_alarm_time_title),
                    subtitle = stringResource(id = R.string.setting_alarm_time_subtitle),
                    content = if (state.alarmTime.isNullOrBlank()) {
                        DEFAULT_ALARM_TIME.toString()
                    } else {
                        state.alarmTime
                    },
                    onClick = { onAlarmTimeClicked() }
                )
            }
            item { // Due Date Format
                SettingsScreenContentItemText(
                    title = stringResource(id = R.string.settings_due_date_format_title),
                    subtitle = stringResource(id = R.string.settings_due_date_format_subtitle),
                    content = state.dueDateFormat.value,
                    onClick = { onDueDateFormatClicked() }
                )
            }

            if (state.car != null) {
                item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
                if (state.car.services.isNotEmpty()) {
                    item { // Delete Car Services
                        SettingsScreenContentItemBase(
                            title = stringResource(id = R.string.setting_delete_all_services_title),
                            subtitle = stringResource(id = R.string.setting_delete_all_services_subtitle),
                            onClick = { onDeleteCarServicesClicked() }
                        )
                    }
                }
                item { // Delete Car Data
                    SettingsScreenContentItemBase(
                        title = stringResource(id = R.string.setting_delete_car_data_title),
                        subtitle = stringResource(id = R.string.setting_delete_car_data_subtitle),
                        onClick = { onDeleteCarDataClicked() }
                    )
                }
            }

            item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
            item { // Import/Export Buttons
                SettingsScreenContentBottom(
                    modifier = Modifier.fillMaxWidth(),
                    onImportClicked = onImportClicked,
                    onExportClicked = onExportClicked
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenContentItemText(
    title: String = "Title",
    subtitle: String = "This is a very long subtitle for explanation.",
    content: String = "due date",
    onClick: () -> Unit = { }
) {
    SettingsScreenContentItemBase(
        title = title,
        subtitle = subtitle,
        onClick = { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenContentItemSwitch(
    title: String = "Title",
    subTitle: String = "This is a very long subtitle for explanation.",
    isChecked: Boolean = true,
    onToggle: (Boolean) -> Unit = { }
) {
    SettingsScreenContentItemBase(
        title = title,
        subtitle = subTitle
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle(it) }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenContentItemBase(
    title: String = "Title",
    subtitle: String = "This is a very long subtitle for explanation.",
    onClick: () -> Unit = { },
    option: @Composable (() -> Unit)? = null
) {
    AppTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 32.dp,
                    end = 32.dp
                )
                .background(color = MaterialTheme.colorScheme.background)
                .clickable { onClick() }
        ) {
            Column(
                Modifier.weight(2f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Light
                )
            }
            option?.let {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    option()
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContentBottom(
    modifier: Modifier = Modifier,
    onImportClicked: () -> Unit = { },
    onExportClicked: () -> Unit = { }
) {
    AppTheme {
        Row(
            modifier = modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                onClick = { onExportClicked() }
            ) {
                Text(text = stringResource(id = R.string.settings_export_data))
            }
            Button(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                onClick = { onImportClicked() }
            ) {
                Text(text = stringResource(id = R.string.settings_import_data))
            }
        }
    }
}