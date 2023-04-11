package com.ivangarzab.carrus.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.databinding.FragmentMapBinding
import com.ivangarzab.carrus.util.delegates.viewBinding
import timber.log.Timber


class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Attempting to fetch map async")
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.mapFragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            Timber.d("Got an async map response")
            setupMap(googleMap)
        } ?: Timber.w("Unable to fetch map async due to nil support fragment")
    }

    private fun setupMap(googleMap: GoogleMap) {
        Timber.d("Setting up map")
        with(googleMap) {
            LatLng(37.778907199662164, -122.39121652883496).let { sf ->
                addMarker(
                    MarkerOptions()
                        .position(sf)
                        .title("San Francisco")
                )
                moveCamera(CameraUpdateFactory.newLatLng(sf))
            }
        }
    }
}