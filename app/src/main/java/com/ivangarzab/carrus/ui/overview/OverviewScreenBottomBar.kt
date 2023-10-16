package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenBottomBar(
    settingsButtonClicked: () -> Unit,
    homeButtonClicked: () -> Unit,
    mapButtonClicked: () -> Unit
) {
    AppTheme {
        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = { },
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
private fun OverviewScreenBottomBarPreview() {
    AppTheme {
        OverviewScreenBottomBar(
            homeButtonClicked = { },
            mapButtonClicked = { },
            settingsButtonClicked = { }
        )
    }
}