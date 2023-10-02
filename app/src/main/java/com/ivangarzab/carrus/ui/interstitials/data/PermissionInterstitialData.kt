package com.ivangarzab.carrus.ui.interstitials.data

import androidx.annotation.StringRes
import com.ivangarzab.carrus.R

/**
 * Created by Ivan Garza Bermea.
 */
data class PermissionInterstitialData(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @StringRes val body: Int,
)

enum class PermissionInterstitialEnum(
    val data: PermissionInterstitialData
) {
    ALARMS(
        PermissionInterstitialData(
            title = R.string.missing_permission_interstitial_alarm_title,
            subtitle = R.string.missing_permission_interstitial_alarm_subtitle,
            body = R.string.missing_permission_interstitial_alarm_content
        )
    ),
    NOTIFICATIONS(
        PermissionInterstitialData(
            title = R.string.missing_permission_interstitial_notification_title,
            subtitle = R.string.missing_permission_interstitial_notification_subtitle,
            body = R.string.missing_permission_interstitial_notification_content
        )
    )
}
