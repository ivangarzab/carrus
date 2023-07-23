package com.ivangarzab.carrus.ui.map

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel(), LifecycleEventObserver {

    private val _state: MutableLiveData<MapState> = MutableLiveData(MapState())
    val state: LiveData<MapState> = _state

    private val autoCompleteSessionToken: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

    private val placesClient = Places.createClient(context)

    private val locationManager = GeoLocationManager(context)

    private var predictionsRequested = true

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            onLocationResultReceived(locationResult)
        }
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> state.value?.let {
                if  (it.isLocationPermissionGranted) {
                    startLocationTracking()
                }
            }
            Lifecycle.Event.ON_PAUSE -> locationManager.stopLocationTracking()
            else -> { /* No-op */ }
        }
    }

    fun onLocationPermissionRequestResult(isGranted: Boolean) {
        Timber.d("Got location permission request result: $isGranted")
        when (isGranted) {
            true -> startLocationTracking()
            false -> { }
        }
    }

    private fun startLocationTracking() {
        locationManager.startLocationTracking(locationCallback)
    }

    fun onLocationResultReceived(locationResult: LocationResult) {
        for (location in locationResult.locations) {
            val latitude = location.latitude
            val longitude = location.longitude
            _state.value = (state.value?.copy(
                currentLocation = LatLng(latitude, longitude)
            ))
            if (predictionsRequested) {
                fetchPlacesPredictions("gas station")
                predictionsRequested = false
            }
        }
    }

    private fun fetchPlacesPredictions(searchString: String) {
        Timber.v("Attempting to fetch predictions with currentLocation=${state.value?.currentLocation ?: "nil"}")
        state.value?.let { state ->
            if (state.currentLocation == null) {
                return
            }

            Timber.d("Attempting to fetch place predictions for search phrase: $searchString")
            // Create search bounds based on current location
            val distance = 10000.0 // in meters
            val bounds = buildRectangleBounds(state.currentLocation, distance)
            val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setCountry("us")
                .setLocationBias(bounds)
                .setOrigin(state.currentLocation)
                .setSessionToken(autoCompleteSessionToken)
                .setQuery(searchString)
                .build()

            placesClient
                .findAutocompletePredictions(predictionsRequest)
                .addOnSuccessListener {
                    onSuccessfulSearchCompleted(it)
                }
                .addOnFailureListener {
                    Timber.e("Google places prediction request not successful. " + it.localizedMessage)
                }
        }
    }

    private fun buildRectangleBounds(from: LatLng, distance: Double): RectangularBounds {
        val southWest = SphericalUtil.computeOffset(from, distance, 225.0)
        val northEast = SphericalUtil.computeOffset(from, distance, 45.0)
        return RectangularBounds.newInstance(southWest, northEast)
    }

    private fun onSuccessfulSearchCompleted(response: FindAutocompletePredictionsResponse) {
        val predictions: List<AutocompletePrediction> = response.autocompletePredictions
        Timber.d("Google places prediction request returned: $predictions")
//        val suggestionList = ArrayList<String>()
        for (i in 1 until predictions.size) {
            val prediction = predictions[i]
            // Filter predictions by restaurant
//            if (prediction.placeTypes.contains(Place.Type.CAR_REPAIR)) {
//            suggestionList.add(prediction.getPrimaryText(null).toString())
            fetchPlaceDetails(prediction.placeId)
//            } else {
//                Timber.w("Found a place that is not a CAR_REPAIR")
//            }
        }
    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
        val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener {
            val place = it.place
            val placeLatLng = place.latLng
            if (placeLatLng != null) {
                state.value?.let { state ->
                    _state.postValue(state.copy(
                        searchList = state.searchList.toMutableList().apply {
                            add(com.ivangarzab.carrus.data.PlaceData(
                                id = "",
                                name = it.place.name ?: "",
                                latLng = it.place.latLng ?: LatLng(0.0, 0.0)
                            ))
                        }
                    ))
                }
            }
        }.addOnFailureListener {
            if (it is ApiException) {
                val apiException = it
                val statusCode = apiException.statusCode
                Timber.e("Place not found, statusCode: $statusCode")
            }
        }
    }
}