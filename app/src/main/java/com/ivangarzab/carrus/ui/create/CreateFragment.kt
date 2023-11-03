package com.ivangarzab.carrus.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.settings.DEFAULT_FILE_MIME_TYPE
import com.ivangarzab.carrus.util.extensions.readFromFile
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class CreateFragment : Fragment() {

    private val viewModel: CreateViewModel by viewModels()

    private val args: CreateFragmentArgs by navArgs()

    @Inject
    lateinit var analytics: Analytics

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let { uri ->
            Timber.d("Got image uri: $uri")
            uri.toString().apply {
                viewModel.onImageUriReceived(uri = this)
            }
        } ?: Timber.d("No media selected")
    }

    private val openDocumentContract = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        Timber.d("Got result from open document contract: ${uri ?: "<nil>"}")
        uri?.let {
            it.readFromFile(requireContext().contentResolver).let { data ->
                data?.let {
                    viewModel.onImportData(data).let { success ->
                        when (success) {
                            true -> toast("Data imported successfully!")
                            false -> toast("Unable to import data")
                        }
                    }
                } ?: Timber.w("Unable to parse data from file with uri: $uri")
            }
        } ?: Timber.w("Unable to read from file with uri: $uri")
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
        analytics.logCreateScreenView(this::class.java.simpleName)
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
}