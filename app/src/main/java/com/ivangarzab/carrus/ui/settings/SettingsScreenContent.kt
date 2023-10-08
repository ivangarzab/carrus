package com.ivangarzab.carrus.ui.settings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.TimeFormat
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import com.ivangarzab.carrus.ui.compose.drawVerticalScrollbar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.settings.data.SettingsState
import com.ivangarzab.carrus.ui.settings.data.SettingsStatePreview

/**
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(SettingsStatePreview::class) state: SettingsState,
    onDarkModeToggle: (Boolean) -> Unit = { },
    onAlarmsToggle: (Boolean) -> Unit = { },
    onAlarmTimeClicked: () -> Unit = { },
    onAlarmFrequencyClicked: () -> Unit = { },
    onDueDateFormatClicked: () -> Unit = { },
    onClockTimeFormatClicked: () -> Unit = { },
    onDeleteCarServicesClicked: () -> Unit = { },
    onDeleteCarDataClicked: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onExportClicked: () -> Unit = { },
    onPrivacyPolicyClicked: () -> Unit = { }
) {
    AppTheme {
        val scrollState = rememberScrollState()
        Column(
            modifier
                .background(color = MaterialTheme.colorScheme.background)
                .drawVerticalScrollbar(scrollState)
                .verticalScroll(
                    state = scrollState,
                    enabled = true
                )
        ) {

            var areAlarmsEnabled: Boolean by rememberSaveable {
                mutableStateOf(value = false)
            }
            areAlarmsEnabled = state.alarmsOn

            SettingsScreenContentItemSwitch(
                title = stringResource(id = R.string.setting_dark_mode_title),
                subTitle = stringResource(id = R.string.setting_dark_mode_subtitle),
                isChecked = isSystemInDarkTheme(),
                onToggle = { onDarkModeToggle(it) }
            )

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)

            SettingsScreenContentItemText(
                title = stringResource(id = R.string.settings_due_date_format_title),
                subtitle = stringResource(id = R.string.settings_due_date_format_subtitle),
                content = state.dueDateFormat.value,
                onClick = { onDueDateFormatClicked() }
            )

            SettingsScreenContentItemText(
                title = stringResource(id = R.string.settings_time_format_title),
                subtitle = stringResource(id = R.string.settings_time_format_subtitle),
                content = state.clockTimeFormat.value,
                onClick = { onClockTimeFormatClicked() }
            )

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)

            SettingsScreenContentItemSwitch(
                title = stringResource(id = R.string.settings_alarm_on_title),
                subTitle = stringResource(id = R.string.settings_alarm_on_subtitle),
                isChecked = areAlarmsEnabled,
                onToggle = { onAlarmsToggle(it) }
            )

            AnimatedVisibility(
                visible = areAlarmsEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    SettingsScreenContentItemText(
                        title = stringResource(id = R.string.setting_alarm_time_title),
                        subtitle = stringResource(id = state.alarmTimeSubtitle),
                        content = state.alarmTime.getTimeAsString(state.clockTimeFormat),
                        onClick = onAlarmTimeClicked
                    )
                    SettingsScreenContentItemText(
                        title = stringResource(id = R.string.setting_alarm_frequency_title),
                        subtitle = stringResource(id = R.string.setting_alarm_frequency_subtitle),
                        content = state.alarmFrequency.value,
                        onClick = onAlarmFrequencyClicked
                    )
                }
            }

            if (state.isThereCarData) {
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            }
            AnimatedVisibility(
                visible = state.isThereCarServicesData,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SettingsScreenContentItemBase(
                    title = stringResource(id = R.string.setting_delete_all_services_title),
                    subtitle = stringResource(id = R.string.setting_delete_all_services_subtitle),
                    onClick = onDeleteCarServicesClicked
                )
            }

            AnimatedVisibility(
                visible = state.isThereCarData,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SettingsScreenContentItemBase(
                    title = stringResource(id = R.string.setting_delete_car_data_title),
                    subtitle = stringResource(id = R.string.setting_delete_car_data_subtitle),
                    onClick = onDeleteCarDataClicked
                )
            }

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)

            SettingsScreenContentItemBase(
                title = stringResource(id = R.string.settings_privacy_policy_title),
                subtitle = stringResource(id = R.string.settings_privacy_policy_subtitle),
                onClick = onPrivacyPolicyClicked
            )

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)

            SettingsScreenContentBottom(
                modifier = Modifier.fillMaxWidth(),
                onImportClicked = onImportClicked,
                onExportClicked = onExportClicked
            )

            SettingsScreenBottomBar(
                modifier = Modifier,
                versionName = BuildConfig.VERSION_NAME
            )
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
private fun SettingsScreenContentItemTime(
    title: String = "Title",
    subtitle: String = "This is a very long subtitle for explanation.",
    time: String = DEFAULT_ALARM_TIME.toString(),
    timeFormat: TimeFormat = TimeFormat.HR24,
    isPM: Boolean = false,
    onClick: () -> Unit = { }
) {
    SettingsScreenContentItemText(
        title = title,
        subtitle = subtitle,
        content = when (timeFormat) {
            TimeFormat.HR24 -> "${time}:00"
            TimeFormat.HR12 -> if (isPM) {
                "${time + 12} PM"
            } else {
                "$time AM"
            } //TODO: We should create a 'Time' data class that abstracts all this logic
        },
        onClick = onClick
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenContentItemSwitch(
    title: String = "Title",
    subTitle: String = "This is a very long subtitle for explanation.",
    isChecked: Boolean = false,
    onToggle: (Boolean) -> Unit = { }
) {
    SettingsScreenContentItemBase(
        title = title,
        subtitle = subTitle
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = MaterialTheme.colorScheme.primary
            )
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
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 32.dp,
                        end = 32.dp
                    )
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
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Light
                        )
                    }
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