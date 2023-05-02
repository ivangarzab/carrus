package com.ivangarzab.carrus.ui.interstitials

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.ivangarzab.carrus.R

/**
 * Created by Ivan Garza Bermea.
 */
class PermissionNotificationInterstitial : PermissionInterstitial() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedData(
            PermissionInterstitialData(
                title = R.string.missing_permission_interstitial_notification_title,
                subtitle = R.string.missing_permission_interstitial_notification_subtitle,
                body = R.string.missing_permission_interstitial_notification_content
            )
        )
    }

    override fun onSettingsClicked() {
        startActivity(
            Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.parse("package:${requireContext().packageName}")
            }
        )
        dismiss()
    }
}