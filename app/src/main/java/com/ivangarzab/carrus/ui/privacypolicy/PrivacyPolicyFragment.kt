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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() {

    @Inject
    lateinit var analytics: Analytics

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