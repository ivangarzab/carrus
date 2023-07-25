package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * This file contains the most common and basic Composables for use across the application.
 *
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit = { }
) {
    AppTheme {
        Dialog(onDismissRequest = onDismissed) {
            Card {
                Column(
                    modifier = modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirmationResult: (Boolean) -> Unit = { },
    text: String = "Delete car data?"
) {
    BaseDialog(onDismissed = { onConfirmationResult(false) }) {
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
        PositiveAndNegativeButtons(
            modifier = Modifier.padding(top = 16.dp),
            positiveButtonText = stringResource(id = R.string.yes),
            onPositiveButtonClicked = { onConfirmationResult(true) },
            negativeButtonText = stringResource(id = R.string.no),
            onNegativeButtonClicked = { onConfirmationResult(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = "App Bar Title",
    isNavigationIconEnabled: Boolean = false,
    onNavigationIconClicked: () -> Unit = { },
    isActionIconEnabled: Boolean = false,
    onActionIconClicked: () -> Unit = { },
    action: @Composable () -> Unit = { }
) {
    AppTheme {
        TopAppBar(
            modifier = modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = title,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                )
            },
            colors = if (isSystemInDarkTheme()) {
                TopAppBarDefaults.topAppBarColors()
            } else {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            },
            navigationIcon = {
                if (isNavigationIconEnabled) {
                    IconButton(onClick = { onNavigationIconClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                            tint = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.onBackground
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    }
                }
            },
            actions = {
                if (isActionIconEnabled) {
                    Text(
                        modifier = Modifier
                            .clickable { onActionIconClicked() },
                        text = "IMPORT",
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                }
            }
        )
    }
}
