package com.ivangarzab.analytics

/**
 * Created by Ivan Garza Bermea.
 */
class TestAnalyticsRepository(
    val logResultCallback: (Boolean) -> Unit
) : AnalyticsRepository {

    override fun logEvent(name: String, vararg params: Pair<String, Any>) {
        var areParamsValid = true
        params.forEach { pair ->
            if (isValidForAnalytics(pair.first).not()) {
                areParamsValid = false
                return@forEach
            }
        }
        logResultCallback(isValidForAnalytics(name) && areParamsValid)
    }

    override fun logScreenView(screenName: String, screenClass: String) {
        if (isValidForAnalytics(screenClass).not() || screenName.isBlank()) {
            logResultCallback(false)
            return
        }
        logEvent(
            name = "screen_view",
            params = arrayOf(
                "screen_name" to screenName,
                "screen_class" to screenClass
            )
        )
    }
}