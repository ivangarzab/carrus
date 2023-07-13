package com.ivangarzab.carrus.ui.interstitials.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Created by Ivan Garza Bermea.
 */
class PermissionInterstitialDataPreview :
    PreviewParameterProvider<PermissionInterstitialData> {
    override val values = sequenceOf(
        PermissionInterstitialEnum.ALARMS.data,
        PermissionInterstitialEnum.NOTIFICATIONS.data
    )
}