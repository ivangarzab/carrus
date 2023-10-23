package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
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

@Composable
fun NavigationBottomBar(
    settingsButtonClicked: () -> Unit,
    homeButtonClicked: () -> Unit,
    mapButtonClicked: () -> Unit
) {
    AppTheme {
        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = homeButtonClicked,
                label = {
                    Text(stringResource(id = R.string.home))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home navigation button"
                    )
                }
            )
            if (App.isRelease().not()) {
                NavigationBarItem(
                    selected = false,
                    onClick = mapButtonClicked,
                    label = {
                        Text(text = "Map")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Home navigation button"
                        )
                    }
                )
            }
            NavigationBarItem(
                selected = false,
                onClick = settingsButtonClicked,
                label = {
                    Text(stringResource(id = R.string.settings))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Home navigation button"
                    )
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationBottomBarPreview() {
    NavigationBottomBar(
        homeButtonClicked = { },
        mapButtonClicked = { },
        settingsButtonClicked = { }
    )
}