package com.ivangarzab.carrus.ui.interstitials

import android.os.Bundle
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
abstract class PermissionInterstitial : DialogFragment(
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

    open fun feedData(data: PermissionInterstitialData) =
        binding.apply {
            title = getString(data.title)
            subTitle = getString(data.subtitle)
            body = getString(data.body)
        }

    abstract fun onSettingsClicked()
}