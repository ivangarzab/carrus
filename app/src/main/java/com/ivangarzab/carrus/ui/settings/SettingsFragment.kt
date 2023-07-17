package com.ivangarzab.carrus.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private val createDocumentsContract = registerForActivityResult(
        ActivityResultContracts.CreateDocument(DEFAULT_FILE_MIME_TYPE)
    ) { uri ->
        uri?.let {
            Timber.d("Got result uri from create document contract: $uri")
            when(viewModel.onExportData(
                requireContext().contentResolver, it
            )) {
                true -> { /* No-op */ }
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
                    onAlarmTimeClicked = {
                        showNumberPickerDialog { numberPicked ->
                            viewModel.onAlarmTimePicked(numberPicked)
                        }
                    },
                    onDueDateFormatClicked = {
                        showDueDateFormatPickerDialog { optionPicked ->
                            viewModel.onDueDateFormatPicked(optionPicked)
                        }
                    },
                    onDeleteCarServicesClicked = {
                        showConfirmationDialog(
                            title = getString(R.string.dialog_delete_services_title),
                            onActionConfirmed = {
                                viewModel.onDeleteServicesClicked()
                            }
                        )
                    },
                    onDeleteCarDataClicked = {
                        showConfirmationDialog(
                            title = getString(R.string.dialog_delete_car_title),
                            onActionConfirmed = {
                                viewModel.onDeleteCarDataClicked()
                            }
                        )
                    },
                    onImportClicked = { openDocumentContract.launch(arrayOf(DEFAULT_FILE_MIME_TYPE)) },
                    onExportClicked = { createDocumentsContract.launch(generateExportFileName()) }
                )
            }
        }
    }

    private fun showNumberPickerDialog(
        onNumberPicked: (String) -> Unit
    ) {
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 23
            displayedValues = viewModel.pickerOptionsAlarmTime
            value = viewModel.getAlarmTime()
        }
        AlertDialog.Builder(requireContext()).apply {
            setView(numberPicker)
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.submit) { dialog, _ ->
                onNumberPicked(numberPicker.displayedValues[numberPicker.value])
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun showDueDateFormatPickerDialog(
        onFormatPicked: (String) -> Unit
    ) {
        val optionPicker = NumberPicker(requireContext()).apply {
            viewModel.pickerOptionsDueDateFormat.let { options ->
                minValue = 0
                maxValue = options.size - 1
                displayedValues = options
                value = if (options.contains(viewModel.getDueDateFormat().value)) {
                    options.indexOf(viewModel.getDueDateFormat().value)
                } else {
                    0
                }
            }
        }
        AlertDialog.Builder(requireContext()).apply {
            setView(optionPicker)
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.submit) { dialog, _ ->
                onFormatPicked(optionPicker.displayedValues[optionPicker.value])
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun showConfirmationDialog(
        title: String,
        onActionConfirmed: () -> Unit
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.yes) { dialog, _ ->
                onActionConfirmed()
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun generateExportFileName(): String =
        EXPORT_FILE_NAME_PREFIX + Calendar.getInstance().getShortenedDate() + EXPORT_FILE_NAME_SUFFIX

    companion object {
        private const val EXPORT_FILE_NAME_PREFIX = "carrus-backup-"
        private const val EXPORT_FILE_NAME_SUFFIX = ".txt"
    }
}

const val DEFAULT_FILE_MIME_TYPE = "text/plain"