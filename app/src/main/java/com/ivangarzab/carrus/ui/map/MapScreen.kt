package com.ivangarzab.carrus.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.ivangarzab.carrus.ui.compose.NavigationBottomBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.map.MapViewModel.Companion.DEFAULT_LOCATION

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(),
    onBackPressed: () -> Unit,
    onNavSettingsPressed: () -> Unit,
    onNavHomePressed: () -> Unit,
    onNavMapPressed: () -> Unit
) {
    val defaultMapZoom = 12f
    val state: MapState by viewModel
        .state
        .observeAsState(initial = MapState())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            state.currentLocation ?: DEFAULT_LOCATION,
            defaultMapZoom
        )
    }
    val uiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            myLocationButtonEnabled = true
        )
    )}
    val properties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true
            )
        )
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.statusBarDarkContentEnabled = true

    AppTheme {
        Scaffold(
            bottomBar = {
                NavigationBottomBar(
                    settingsButtonClicked = onNavSettingsPressed,
                    homeButtonClicked = onNavHomePressed,
                    mapButtonClicked = onNavMapPressed
                )
            }
        ) {
            //TODO: Impl back button
            GoogleMap(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = properties
            )
        }
    }
}