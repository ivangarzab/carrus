package com.ivangarzab.carrus.ui.interstitials

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.databinding.FragmentInterstitialNotificationsBinding

/**
 * Created by Ivan Garza Bermea.
 */
class PermissionNotificationInterstitial : DialogFragment(
    R.layout.fragment_interstitial_notifications
) {

    private lateinit var binding: FragmentInterstitialNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInterstitialNotificationsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            this.setSettingsClickListener { onSettingsClicked() }
            this.setNotNowClickListener { dismiss() }
        }
    }

    private fun onSettingsClicked() {
        startActivity(
            Intent().apply {
                action = ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.parse("package:${requireContext().packageName}")
            }
        )
        dismiss()
    }
}