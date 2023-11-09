package com.ivangarzab.carrus.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.create.CreateFragmentDirections
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.managers.Analytics
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var analytics: Analytics

    private val createDocumentsContract = registerForActivityResult(
        ActivityResultContracts.CreateDocument(DEFAULT_FILE_MIME_TYPE)
    ) { uri ->
        uri?.let {
            Timber.d("Got result uri from create document contract: $uri")
            when(viewModel.onExportData(
                requireContext().contentResolver, it
            )) {
                true -> { toast("Data export success") }
                false -> toast("Unable to export data")
            }
        } ?: Timber.w("Error fetching uri")
    }

    private val openDocumentContract = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            Timber.d("Got result uri from open document contract: $it")
            toast(when (viewModel.onImportData(
                requireContext().contentResolver, it
            )) {
                true -> "Data import successful"
                false -> "Unable to import data"
            })

        } ?: Timber.w("Unable to read from file with uri")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            AppTheme {
                SettingsScreenStateful(
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
                        openDocumentContract.launch(arrayOf(DEFAULT_FILE_MIME_TYPE))
                                      },
                    onExportClicked = {
                        analytics.logExportButtonClicked()
                        createDocumentsContract.launch(generateExportFileName())
                                      },
                    onPrivacyPolicyClicked = {
                        analytics.logPrivacyPolicyClicked()
                        findNavController().navigate(
                            SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment()
                        )
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            onRequestAlarmPermission.observe(viewLifecycleOwner) {
                findNavController().navigate(
                    SettingsFragmentDirections.actionSettingsFragmentToAlarmPermissionModal()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logSettingsScreenView(this@SettingsFragment::class.java.simpleName)
    }

    private fun generateExportFileName(): String =
        Calendar.getInstance().getShortenedDate() + EXPORT_FILE_NAME_SUFFIX + EXPORT_FILE_NAME_EXTENSION

    companion object {
        private const val EXPORT_FILE_NAME_SUFFIX = "-carrus-backup"
        private const val EXPORT_FILE_NAME_EXTENSION = ".txt"
    }
}

const val DEFAULT_FILE_MIME_TYPE = "text/plain"