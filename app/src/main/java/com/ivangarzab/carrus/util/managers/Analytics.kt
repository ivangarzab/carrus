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

    // Instrumentation events
    fun logCarCreate(id: String, name: String) {
        logEvent("car_create",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logCarDelete(id: String, name: String) {
        logEvent("car_delete",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logCarExported(id: String, name: String) {
        logEvent("car_exported",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logCarImported(id: String, name: String) {
        logEvent("car_Imported",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logServiceCreate(id: String, name: String) {
        logEvent("service_create",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logServiceDelete(id: String, name: String) {
        logEvent("service_delete",
            Pair("id", id),
            Pair("name", name)
        )
    }

    fun logServiceListSort(type: String) {
        logEvent("service_list_sort",
            Pair("type", type)
        )
    }

    fun logAlarmSchedule(name: String, force: Boolean) {
        logEvent("alarm_schedule",
            Pair("type", name),
            Pair("force", force)
        )
    }

    fun logAlarmCancel(name: String) {
        logEvent("alarm_cancel",
            Pair("type", name)
        )
    }

    fun logAlarmFeatureToggle(enabled: Boolean) {
        logEvent("alarm_feature_toggle",
            Pair("enabled", enabled)
        )
    }

    fun logAlarmTimeChange(alarmTime: Int) {
        logEvent("alarm_time_change",
            Pair("alarmTime", alarmTime)
        )
    }

    fun logAlarmFrequencySet(frequency: String) {
        logEvent("alarm_frequency_change",
            Pair("alarmFrequency", frequency)
        )
    }

    fun logNightThemeChange(isNight: Boolean) {
        logEvent("night_theme_change",
            Pair("isNight", isNight)
        )
    }

    fun logDueDateFormatChange(format: String) {
        logEvent("due_date_format_change",
            Pair("format", format)
        )
    }

    fun logTimeFormatChange(format: String) {
        logEvent("time_format_change",
            Pair("format", format)
        )
    }

    fun logLeftHandedChange(lefty: Boolean) {
        logEvent("left_handed_change",
            Pair("isLefty", lefty)
        )
    }
}