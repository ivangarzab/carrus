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
import androidx.navigation.findNavController
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.managers.Analytics
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MapFragment : Fragment() {

    private val viewModel: MapViewModel by viewModel()

    val analytics: Analytics by inject()

    @SuppressLint("MissingPermission")
    private val locationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.onLocationPermissionRequestResult(it)
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
}