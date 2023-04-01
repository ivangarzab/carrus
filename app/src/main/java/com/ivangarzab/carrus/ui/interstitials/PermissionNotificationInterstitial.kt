package com.ivangarzab.carrus.ui.interstitials

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.databinding.ModalInterstitialPermissionBinding
import com.ivangarzab.carrus.util.extensions.clearBackgroundForRoundedCorners

/**
 * Created by Ivan Garza Bermea.
 */
class PermissionNotificationInterstitial : DialogFragment(
    R.layout.modal_interstitial_permission
) {

    private lateinit var binding: ModalInterstitialPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalInterstitialPermissionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.dialog?.clearBackgroundForRoundedCorners()
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