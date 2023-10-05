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

    private fun logEvent(name: String, vararg params: Pair<String, Any>) {
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

    fun logPermissionGranted(permission: String) {
        logEvent("permission_granted",
            Pair("permission_type", permission)
        )
    }

    fun logPermissionDenied(permission: String) {
        logEvent("permission_denied",
            Pair("permission_type", permission)
        )
    }

    // Instrumentation events
    fun logCarCreated(id: String, name: String) {
        logEvent("car_created",
            Pair("car_id", id),
            Pair("name", name)
        )
    }

    fun logCarDeleted(id: String, name: String) {
        logEvent("car_deleted",
            Pair("car_id", id),
            Pair("name", name)
        )
    }

    fun logCarExported(id: String, name: String) {
        logEvent("car_exported",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logCarImported(id: String, name: String) {
        logEvent("car_imported",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logServiceCreated(id: String, name: String) {
        logEvent("service_created",
            Pair("service_id", id),
            Pair("service_name", name)
        )
    }

    fun logServiceDeleted(id: String, name: String) {
        logEvent("service_deleted",
            Pair("service_id", id),
            Pair("service_name", name)
        )
    }

    fun logServiceListSorted(type: String) {
        logEvent("service_list_sorted",
            Pair("sort_type", type)
        )
    }

    fun logServiceListDeleted(carId: String) {
        logEvent("service_list_deleted",
            Pair("car_id", carId)
        )
    }

    fun logAlarmScheduled(name: String, force: Boolean) {
        logEvent("alarm_scheduled",
            Pair("alarm_type", name),
            Pair("force", force)
        )
    }

    fun logAlarmCancelled(name: String) {
        logEvent("alarm_cancelled",
            Pair("alarm_type", name)
        )
    }

    fun logAlarmFeatureToggled(enabled: Boolean) {
        logEvent("alarm_feature_toggled",
            Pair("enabled", enabled)
        )
    }

    fun logAlarmTimeChanged(alarmTime: Int) {
        logEvent("alarm_time_changed",
            Pair("alarm_time", alarmTime)
        )
    }

    fun logAlarmFrequencyChanged(frequency: String) {
        logEvent("alarm_frequency_changed",
            Pair("alarm_frequency", frequency)
        )
    }

    fun logNightThemeChanged(isNight: Boolean) {
        logEvent("night_theme_changed",
            Pair("night", isNight)
        )
    }

    fun logDueDateFormatChanged(format: String) {
        logEvent("due_date_format_changed",
            Pair("format", format)
        )
    }

    fun logTimeFormatChanged(format: String) {
        logEvent("time_format_changed",
            Pair("format", format)
        )
    }

    fun logLeftHandedChanged(lefty: Boolean) {
        logEvent("left_handed_changed",
            Pair("lefty", lefty)
        )
    }
}