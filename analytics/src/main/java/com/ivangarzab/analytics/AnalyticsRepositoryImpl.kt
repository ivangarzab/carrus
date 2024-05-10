package com.ivangarzab.analytics

import androidx.annotation.VisibleForTesting
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class AnalyticsRepositoryImpl : AnalyticsRepository {

    @VisibleForTesting
    var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        Timber.v("Starting up Firebase Analytics repository")
    }

    override fun logEvent(name: String, vararg params: Pair<String, Any>) {
        if (isValidForAnalytics(name).not()) return
        params.forEach { if (isValidForAnalytics(it.first).not()) return }

        printLoggedEvent(name, *params) //TODO: call Timber.a() instead
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
        if (isValidForAnalytics(screenClass).not() || screenName.isBlank()) return
        logEvent(
            name = FirebaseAnalytics.Event.SCREEN_VIEW,
            params = arrayOf(
                FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                FirebaseAnalytics.Param.SCREEN_CLASS to screenClass
            )
        )
    }

    private fun printLoggedEvent(name: String, vararg params: Pair<String, Any>) {
        var loggedEvent = "Logging event '$name'"
        params.forEachIndexed { index, pair ->
            loggedEvent += "${if (index > 0) " -" else " |"} '${pair.first}'=${pair.second}"
        }
        Timber.v(loggedEvent)
    }
}