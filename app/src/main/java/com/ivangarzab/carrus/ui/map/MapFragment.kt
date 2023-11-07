package com.ivangarzab.carrus.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivangarzab.carrus.MainActivity
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.databinding.FragmentMapBinding
import com.ivangarzab.carrus.util.delegates.viewBinding
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var analytics: Analytics

    private lateinit var map: GoogleMap

    private var currentLocationSet = false

    @SuppressLint("MissingPermission")
    private val locationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.onLocationPermissionRequestResult(it)
        if (it) map.isMyLocationEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        analytics.logMapScreenView(this::class.java.simpleName)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupViews()
        setupMap()
        requestLocationPermission()

        viewModel.apply {
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
        }
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(
            (requireActivity() as MainActivity).getBindingRoot()
        ) { _, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                binding.mapRoot.apply {
                    updatePadding(bottom = insets.bottom)
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            mapGasStationButton.setOnClickListener {
                viewModel.onSearchForGasStation()
            }
            mapCarWashButton.setOnClickListener {
                viewModel.onSearchForCarWash()
            }
            mapRepairShopButton.setOnClickListener {
                viewModel.onSearchForRepairShop()
            }
        }
    }

    private fun requestLocationPermission() {
        Timber.d("Requesting location permission")
        locationPermissionRequestLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun setupMap() {
        Timber.d("Attempting to fetch map async")
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.mapFragment
        ) as? SupportMapFragment

        mapFragment?.getMapAsync { googleMap ->
            Timber.d("Got an async map response")
            map = googleMap
        } ?: Timber.w("Unable to fetch map async due to nil support fragment")
    }

    private fun placeMarker(location: LatLng, title: String?) {
        Timber.v("Placing location '$title' at $location")
        with(map) {
            addMarker(
                MarkerOptions()
                    .position(location)
                    .title(title)
            )
        }
    }

    // Search bar suggestion click handler
    /*override fun OnItemClickListener(position: Int, v: View?) {
        // Get selected prediction
        if (position < predictionsList.size - 1) {
            val selectedPrediction = predictionsList[position]
            // Get the details for this place
            fetchPlaceDetails(selectedPrediction.placeId)
        }
    }*/

    companion object {
        private const val MAP_ZOOM = 18F
    }
}