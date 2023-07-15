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
import com.ivangarzab.carrus.util.extensions.readFromFile
import com.ivangarzab.carrus.util.extensions.toast
import com.ivangarzab.carrus.util.extensions.writeInFile
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private val createDocumentsContract = registerForActivityResult(
        ActivityResultContracts.CreateDocument(DEFAULT_FILE_MIME_TYPE)
    ) { uri ->
        //TODO: Move all this into the VM
        Timber.d("Got result from create document contract: ${uri ?: "<nil>"}")
        uri?.let {
            viewModel.getExportData()?.let {
                //TODO: Send this piece into a working thread
                uri.writeInFile(requireContext().contentResolver, it)
            } ?: toast("Unable to export data")
        } ?: Timber.w("Error fetching uri")
    }

    private val openDocumentContract = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        //TODO: Move all this into the VM
        Timber.d("Got result from open document contract: ${uri ?: "<nil>"}")
        uri?.let {
            it.readFromFile(requireContext().contentResolver).let { data ->
                data?.let {
                    viewModel.onImportData(data).let { success ->
                        toast(when (success) {
                            true -> "Data import successful"
                            false -> "Unable to import data"
                        })
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
                    onExportClicked = { createDocumentsContract.launch(DEFAULT_EXPORT_FILE_NAME) }
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

    companion object {
        private const val DEFAULT_EXPORT_FILE_NAME = "carrus-backup.txt"
    }
}

const val DEFAULT_FILE_MIME_TYPE = "text/plain"