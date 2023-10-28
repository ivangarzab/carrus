package com.ivangarzab.carrus.ui.modals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class ServiceModalFragment : BottomSheetDialogFragment() {

    private val args: ServiceModalFragmentArgs by navArgs()

    @Inject
    lateinit var analytics: Analytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            ServiceModalScreenStateful(
                args = args,
                onSubmissionSuccess = { submitSuccess ->
                    analytics.logServiceSubmitClicked()
                    when (submitSuccess) {
                        true -> findNavController().popBackStack()
                        false -> toast("Missing required data")
                    }

                }
            )
        }
    }
}