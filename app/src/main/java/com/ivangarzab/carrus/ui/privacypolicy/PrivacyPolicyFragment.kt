package com.ivangarzab.carrus.ui.privacypolicy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.managers.Analytics
import org.koin.android.ext.android.inject

/**
 * Created by Ivan Garza Bermea.
 */
class PrivacyPolicyFragment : Fragment() {

    val analytics: Analytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            AppTheme {
                PrivacyPolicyScreen(
                    url = WEBVIEW_DEFAULT_URL,
                    onBackButtonClicked = { findNavController().popBackStack() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logPrivacyPolicyScreenView(this@PrivacyPolicyFragment::class.java.simpleName)
    }

    companion object {
        private const val WEBVIEW_DEFAULT_URL = "https://carrus.app/privacy-policy/"
    }
}