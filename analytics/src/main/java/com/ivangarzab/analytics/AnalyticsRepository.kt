package com.ivangarzab.analytics

/**
 * Created by Ivan Garza Bermea.
 */
interface AnalyticsRepository {
    fun logEvent(name: String, vararg params: Pair<String, Any>)
    fun logScreenView(screenName: String, screenClass: String)
}