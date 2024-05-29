package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.providers.DebugFlagProvider
import com.ivangarzab.carrus.data.structures.LiveState
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

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
    isNavigationIconEnabled: Boolean = true,
    onNavigationIconClicked: () -> Unit = { },
    isActionIconEnabled: Boolean = false,
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
                            modifier = Modifier.size(28.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
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
            actions = { if (isActionIconEnabled) action() }
        )
    }
}

@Composable
fun NavigationBottomBar(
    viewModel: NavigationBottomBarViewModel = koinViewModel(),
    settingsButtonClicked: () -> Unit,
    homeButtonClicked: () -> Unit,
    mapButtonClicked: () -> Unit
) {
    val state: NavigationBottomBarViewModel.NavigationBottomBarState by viewModel
        .state
        .observeAsState(initial = NavigationBottomBarViewModel.NavigationBottomBarState())

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
                        modifier = Modifier.size(28.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home),
                        contentDescription = "Home navigation button"
                    )
                }
            )
            if (state.showMapButton) {
                NavigationBarItem(
                    selected = false,
                    onClick = mapButtonClicked,
                    label = {
                        Text(text = "Map")
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_map),
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
                        modifier = Modifier.size(28.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
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

//TODO: Move into its own file
class NavigationBottomBarViewModel(
    debugFlagProvider: DebugFlagProvider
) : ViewModel() {
    data class NavigationBottomBarState(
        val showMapButton: Boolean = false
    )
    val state: LiveState<NavigationBottomBarState> = LiveState(
        NavigationBottomBarState(
            showMapButton = debugFlagProvider.isDebugEnabled()
        )
    )
}