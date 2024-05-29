package com.ivangarzab.carrus.ui.interstitials

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialEnum

/**
 * Created by Ivan Garza Bermea.
 */
@RequiresApi(Build.VERSION_CODES.S)
class PermissionAlarmInterstitial :
    PermissionInterstitial<PermissionInterstitialViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PermissionInterstitialViewModel::class.java].apply {
            init(PermissionInterstitialEnum.ALARMS)
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logAlarmInterstitialScreenView(this@PermissionAlarmInterstitial::class.java.simpleName)
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