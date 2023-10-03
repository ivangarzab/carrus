package com.ivangarzab.carrus.util.managers

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
object Analytics {

    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        Timber.v("Starting up Firebase Analytics")
    }

    private fun logEvent(name: String, vararg params: Pair<String, String>) {
        firebaseAnalytics.logEvent(name) {
            params.forEach {
                param(it.first, it.second)
            }
        }
    }

    // Instrumentation events


}