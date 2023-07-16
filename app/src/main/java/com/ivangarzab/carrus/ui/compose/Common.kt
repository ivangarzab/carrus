package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * This file contains the most common and basic Composables for use across the application.
 *
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PositiveButton(
    modifier: Modifier = Modifier,
    text: String = "ACTION",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NegativeButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        TextButton(
            modifier = modifier
                .fillMaxWidth(),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AppTheme {
        Card {
            Column(modifier = modifier.padding(40.dp)) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopBar(
    title: String = "App Bar Title",
    isNavigationIconEnabled: Boolean = false,
    onNavigationIconClicked: () -> Unit = { },
    isActionIconEnabled: Boolean = false,
    onActionIconClicked: () -> Unit = { },
    action: @Composable () -> Unit = { }
) {
    AppTheme {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
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
                        modifier = Modifier,
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
