package com.ivangarzab.carrus.ui.interstitials

import androidx.annotation.StringRes

/**
 * Created by Ivan Garza Bermea.
 */
data class PermissionInterstitialData(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @StringRes val body: Int,
)
