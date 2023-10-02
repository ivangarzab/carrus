package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenBottomBar(
    actionButtonClicked: () -> Unit = { },
    settingsButtonClicked: () -> Unit = { },
    carEditButtonClicked: () -> Unit = { },
    carDetailsButtonClicked: () -> Unit = { },
    mapButtonClicked: () -> Unit = { }
) {
    AppTheme {
        BottomAppBar(
            modifier = Modifier,
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    contentColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    onClick = actionButtonClicked
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add Service floating action button"
                    )
                }
            },
            actions = {
                val iconSize: Dp = 32.dp
                val iconTint = MaterialTheme.colorScheme.primary
                val iconModifier: Modifier = Modifier.padding(start = 3.dp)
                IconButton(
                    modifier = iconModifier,
                    onClick = carEditButtonClicked
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.Edit,
                        tint = iconTint,
                        contentDescription = "Edit icon button"
                    )
                }
                IconButton(
                    modifier = iconModifier,
                    onClick = carDetailsButtonClicked
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.Info,
                        tint = iconTint,
                        contentDescription = "Car details icon button"
                    )
                }
                IconButton(
                    modifier = iconModifier,
                    onClick = settingsButtonClicked
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.Settings,
                        tint = iconTint,
                        contentDescription = "Edit Car icon button"
                    )
                }
                IconButton(
                    modifier = iconModifier,
                    onClick = mapButtonClicked
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.ic_map),
                        tint = iconTint,
                        contentDescription = "Map icon button"
                    )
                }
            }
        )
    }
}