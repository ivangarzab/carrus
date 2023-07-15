package com.ivangarzab.carrus.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    onDarkModeToggle: () -> Unit = { },
    importClick: () -> Unit = { },
    exportClick: () -> Unit = { }
) {
    AppTheme {
        LazyColumn(
            modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            item {
                SettingsScreenContentItemSwitch(
                    title = stringResource(id = R.string.setting_dark_mode_title),
                    subTitle = stringResource(id = R.string.setting_dark_mode_subtitle),
                    onToggle = { onDarkModeToggle() }
                )
            }
            item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
            item {
                SettingsScreenContentItemText(
                    title = stringResource(id = R.string.setting_alarm_time_title),
                    subTitle = stringResource(id = R.string.setting_alarm_time_subtitle),
                    content = "6"
                )
            }
            item {
                SettingsScreenContentItemText(
                    title = stringResource(id = R.string.settings_due_date_format_title),
                    subTitle = stringResource(id = R.string.settings_due_date_format_subtitle),
                    content = "days"
                )
            }
            item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
            item {
                SettingsScreenContentItemBase(
                    title = stringResource(id = R.string.setting_delete_all_services_title),
                    subTitle = stringResource(id = R.string.setting_delete_all_services_subtitle)
                )
            }
            item {
                SettingsScreenContentItemBase(
                    title = stringResource(id = R.string.setting_delete_car_data_title),
                    subTitle = stringResource(id = R.string.setting_delete_car_data_subtitle)
                )
            }
            item { Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface) }
            item {
                SettingsScreenContentBottom(
                    modifier = Modifier.fillMaxWidth(),
                    importClick = importClick,
                    exportClick = exportClick
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
    subTitle: String = "This is a very long subtitle for explanation.",
    content: String = "6"
) {
    SettingsScreenContentItemBase(title, subTitle) {
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
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
    onToggle: () -> Unit = { }
) {
    SettingsScreenContentItemBase(title, subTitle) {
        Switch(
            checked = false,
            onCheckedChange = { onToggle() }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenContentItemBase(
    title: String = "Title",
    subTitle: String = "This is a very long subtitle for explanation.",
    option: @Composable (() -> Unit)? = null
) {
    AppTheme {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 32.dp,
                    end = 32.dp
                )
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                Modifier.weight(2f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
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
    importClick: () -> Unit = { },
    exportClick: () -> Unit = { }
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
                onClick = { exportClick() }
            ) {
                Text(text = "EXPORT DATA")
            }
            Button(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                onClick = { importClick() }
            ) {
                Text(text = "IMPORT DATA")
            }
        }
    }
}