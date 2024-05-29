package com.ivangarzab.carrus.ui.modal_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carrus.ui.modals.ServiceModalFragmentArgs
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.managers.Analytics
import org.koin.android.ext.android.inject

/**
 * Created by Ivan Garza Bermea.
 */
@Deprecated("Moved into Compose: See ServiceModalSheet")
class ServiceModalFragment : BottomSheetDialogFragment() {

    private val args: ServiceModalFragmentArgs by navArgs()

    val analytics: Analytics by inject()

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
                        false -> toast("Missing required field(s) or wrong data", true)
                    }

                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logServiceModalScreenView(this@ServiceModalFragment::class.java.simpleName)
    }
}