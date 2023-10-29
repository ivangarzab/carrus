package com.ivangarzab.analytics

/**
 * Created by Ivan Garza Bermea.
 */
class TestAnalyticsRepository(
    val logResultCallback: (Boolean) -> Unit
) : AnalyticsRepository {

    override fun logEvent(name: String, vararg params: Pair<String, Any>) {
        logResultCallback(isValidForAnalytics(name))
    }

    override fun logScreenView(screenName: String, screenClass: String) {
        logResultCallback(isValidForAnalytics(screenName) && isValidForAnalytics(screenClass))
    }
}