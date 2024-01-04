package com.ivangarzab.carrus.data.models

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaceDataTest {

    @Test
    fun `test PlaceData properties`() {
        val placeId = "place_id"
        val placeName = "Sample Place"
        val placeLatLng = LatLng(40.7128, -74.0060) // Sample LatLng coordinates

        val placeData = PlaceData(placeId, placeName, placeLatLng)

        assertEquals(placeId, placeData.id)
        assertEquals(placeName, placeData.name)
        assertEquals(placeLatLng, placeData.latLng)
    }
}
