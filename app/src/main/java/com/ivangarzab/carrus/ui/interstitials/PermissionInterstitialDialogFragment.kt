package com.ivangarzab.carrus.ui.interstitials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.extensions.clearBackgroundForRoundedCorners
import com.ivangarzab.carrus.util.managers.Analytics
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
abstract class PermissionInterstitial<T : PermissionInterstitialViewModel> :
    DialogFragment() {

    lateinit var viewModel: T

    @Inject
    lateinit var analytics: Analytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                AppTheme {
                    PermissionInterstitialScreen(
                        viewModel = viewModel,
                        positiveButtonClick = { onSettingsClicked() },
                        negativeButtonClick = { dismiss() }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.dialog?.clearBackgroundForRoundedCorners()
    }

    abstract fun onSettingsClicked()
}