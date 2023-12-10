package com.ivangarzab.carrus.ui.map

import com.google.android.gms.maps.model.LatLng
import com.ivangarzab.carrus.data.models.PlaceData

/**
 * Created by Ivan Garza Bermea.
 */
data class MapState(
    val isLocationPermissionGranted: Boolean = false,
    val currentLocation: LatLng? = null,
    val searchList: List<PlaceData> = emptyList()
)
