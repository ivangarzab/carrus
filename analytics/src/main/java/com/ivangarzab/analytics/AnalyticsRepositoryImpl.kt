package com.ivangarzab.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class AnalyticsRepositoryImpl @Inject constructor() : AnalyticsRepository {

    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        Timber.v("Starting up Firebase Analytics repository")
    }

    override fun logEvent(name: String, vararg params: Pair<String, Any>) {
        Timber.v("Logging event $name")
        firebaseAnalytics.logEvent(name) {
            params.forEach {
                when (it.second) {
                    is Long, Int -> param(it.first, it.second as Long)
                    is Double, Float -> param(it.first, it.second as Double)
                    else -> param(it.first, it.second.toString())
                }
            }
        }
    }

    override fun logScreenView(screenName: String, screenClass: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }
}