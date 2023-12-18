package com.ivangarzab.carrus.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment() {

    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var analytics: Analytics

    @SuppressLint("MissingPermission")
    private val locationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.onLocationPermissionRequestResult(it)
//        if (it) map.isMyLocationEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            AppTheme {
                MapScreen(
                    onBackPressed = { findNavController().popBackStack() },
                    onNavSettingsPressed = {
                        findNavController().navigate(
                            MapFragmentDirections.actionGlobalSettingsFragment()
                        )
                    },
                    onNavHomePressed = {
                        findNavController().navigate(
                            MapFragmentDirections.actionNavGraphSelf()
                        )
                    },
                    onNavMapPressed = {
                        findNavController().navigate(
                            MapFragmentDirections.actionGlobalMapFragment()
                        )
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupMap()
        requestLocationPermission()

        /*viewModel.apply {
            lifecycle.addObserver(this)
            state.observe(viewLifecycleOwner) { state ->
                // Set current location
                state.currentLocation?.let {
                    if (currentLocationSet.not()) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, MAP_ZOOM))
                        currentLocationSet = true
                    }
                }
                // Mark all search results
                if (state.searchList.isNotEmpty()) {
                    state.searchList.forEach { item ->
                        with(viewModel) {
                            if (isPlaceMarked(item).not()) {
                                placeMarker(item.latLng, item.name)
                                onPlaceMarked(item)
                            }
                        }
                    }
                }
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        analytics.logMapScreenView(this@MapFragment::class.java.simpleName)
    }

    private fun requestLocationPermission() {
        Timber.d("Requesting location permission")
        locationPermissionRequestLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun placeMarker(location: LatLng, title: String?) {
        Timber.v("Placing location '$title' at $location")
        val map: GoogleMap? = null
        with(map!!) {
            addMarker(
                MarkerOptions()
                    .position(location)
                    .title(title)
            )
        }
    }
}