package com.ivangarzab.carrus.ui.map

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Derived from:
 * [https://github.com/justmobiledev/android-kotlin-restaurant-search-1/blob/main/app/src/main/java/com/mobiledeveloperblog/restaurantsearch1/location/LocationManager.kt]
 *
 * Created by Ivan Garza Bermea.
 */
class GeoLocationManager(context: Context) {

    private val weakContext = WeakReference(context)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback

    private var locationRequest= LocationRequest()

    private var startedLocationTracking = false

    init {
        configureLocationRequest()
        setupLocationProviderClient()
    }

    private fun setupLocationProviderClient() {
        weakContext.get()?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        } ?: Timber.w("Unable to setup location provider due to missing context")
    }

    private fun configureLocationRequest() {
        locationRequest.interval = UPDATE_INTERVAL_MILLISECONDS
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun startLocationTracking(locationCallback: LocationCallback) {
        Timber.d("Starting to track location")
        if (!startedLocationTracking) {
            //noinspection MissingPermission
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())

            this.locationCallback = locationCallback

            startedLocationTracking = true
        }
    }

    fun stopLocationTracking() {
        Timber.d("Location tracking halted")
        if (startedLocationTracking) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    companion object {
        const val UPDATE_INTERVAL_MILLISECONDS: Long = 0
        const val FASTEST_UPDATE_INTERVAL_MILLISECONDS = UPDATE_INTERVAL_MILLISECONDS / 2
    }
}