package com.ivangarzab.carrus.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.databinding.FragmentMapBinding
import com.ivangarzab.carrus.util.delegates.viewBinding
import timber.log.Timber


class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Attempting to fetch map async")
        val mapFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            Timber.d("Got an async map")
            setupMap(googleMap)
        }
    }

    private fun setupMap(googleMap: GoogleMap) {
        Timber.d("Setting up map")
        val sf = LatLng(37.7749, -122.4194)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        // Add a marker on the map coordinates.
        /*googleMap.addMarker(
        MarkerOptions()
            .position(sf)
            .title("San Francisco")
        )*/
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sf))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}