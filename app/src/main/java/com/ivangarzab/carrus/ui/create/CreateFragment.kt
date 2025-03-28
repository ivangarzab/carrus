package com.ivangarzab.carrus.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.settings.DEFAULT_FILE_MIME_TYPE
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.managers.Analytics
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class CreateFragment : Fragment() {

    private val viewModel: CreateViewModel by viewModel()

    private val args: CreateFragmentArgs by navArgs()

    val analytics: Analytics by inject()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        //TODO: Move null check to VM
        it?.let { uri ->
            Timber.d("Got image uri: $uri")
            viewModel.onImageUriReceived(uri = uri.toString())
        } ?: Timber.d("No media selected")
    }

    private val openDocumentContract = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        Timber.d("Got result from open document contract: $uri")
        //TODO: Move null check to VM
        uri?.let {
            viewModel.onImportData(it).let { success ->
                when (success) {
                    true -> toast("Data imported successfully!")
                    false -> toast("Unable to import data")
                }
            }
        } ?: Timber.w("Unable to read from null uri")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            AppTheme {
                CreateScreenStateful(
                    onBackPressed = { findNavController().popBackStack() },
                    onNavHomePressed = { findNavController().navigate(
                        CreateFragmentDirections.actionNavGraphSelf()
                    ) },
                    onNavSettingsPressed = { findNavController().navigate(
                        CreateFragmentDirections.actionGlobalSettingsFragment()
                    ) },
                    onNavMapPressed = { findNavController().navigate(
                        CreateFragmentDirections.actionGlobalMapFragment()
                    ) },
                    onImportClicked = {
                        analytics.logImportButtonClicked()
                        openDocumentContract.launch(
                            arrayOf(DEFAULT_FILE_MIME_TYPE)
                        )
                    },
                    onAddImageClicked = {
                        analytics.logAddImageClicked()
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            init(args.data)
            onSubmit.observe(viewLifecycleOwner) { success ->
                if (success) {
                    Timber.v("Navigating back to Overview fragment")
                    findNavController().popBackStack()
                }
            }
            viewModel.onVerify.observe(viewLifecycleOwner) { verificationSuccess ->
                if (verificationSuccess.not()) {
                    toast("Missing required fields")
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logCreateScreenView(this@CreateFragment::class.java.simpleName)
    }
}