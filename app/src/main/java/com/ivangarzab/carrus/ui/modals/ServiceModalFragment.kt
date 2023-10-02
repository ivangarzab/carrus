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
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class ServiceModalFragment : BottomSheetDialogFragment() {

    private val args: ServiceModalFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            ServiceModalScreenStateful(
                args = args,
                onSubmissionSuccess = { submitSuccess ->
                    when (submitSuccess) {
                        true -> findNavController().popBackStack()
                        false -> toast("Missing required data")
                    }

                }
            )
        }
    }
}