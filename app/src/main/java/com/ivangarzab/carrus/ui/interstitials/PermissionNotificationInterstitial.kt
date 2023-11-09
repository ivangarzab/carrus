package com.ivangarzab.carrus.ui.interstitials

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialEnum
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class PermissionNotificationInterstitial :
    PermissionInterstitial<PermissionInterstitialViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PermissionInterstitialViewModel::class.java].apply {
            init(PermissionInterstitialEnum.NOTIFICATIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logNotificationInterstitialScreenView(this@PermissionNotificationInterstitial::class.java.simpleName)
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