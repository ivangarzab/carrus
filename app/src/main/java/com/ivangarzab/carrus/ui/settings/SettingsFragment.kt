package com.ivangarzab.carrus.ui.settings

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.BuildConfig
import com.ivangarzab.carrus.MainActivity
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME
import com.ivangarzab.carrus.databinding.FragmentSettingsBinding
import com.ivangarzab.carrus.util.delegates.viewBinding
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
        setupViews()

        viewModel.state.observe(viewLifecycleOwner) {
            Timber.d("Got new Car state: ${it.car ?: "null"}")
            it?.let { state ->
                binding.apply {
                    car = state.car
                    state.alarmTime?.let {time ->
                        alarmTime = viewModel.getTimeString(
                            when (time.isBlank()) {
                                true -> DEFAULT_ALARM_TIME
                                false -> time.toInt()
                            }
                        )
                    }
                    versionNumber = "v${BuildConfig.VERSION_NAME}"
                    dueDateFormat = state.dueDateFormat.value
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.settingsDarkModeOption.settingsOptionToggle.isChecked = viewModel.isNight()
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(
            (requireActivity() as MainActivity).getBindingRoot()
        ) { _, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                binding.settingsAppBarLayout.apply {
                    updatePadding(top = insets.top)
                }
                binding.settingsVersionNumber.updatePadding(
                    bottom = insets.bottom
                )
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.settingsToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white)
            navigationIcon?.setTint(Color.WHITE)
            setNavigationOnClickListener {
                Timber.v("Navigating back to the Overview fragment")
                findNavController().popBackStack()
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            settingsDarkModeOption.settingsOptionToggle.apply {
                setOnClickListener {
                    viewModel.onDarkModeToggleClicked(isChecked)
                }
            }
            settingsDeleteCarOption.root.setOnClickListener {
                showConfirmationDialog(
                    title = getString(R.string.dialog_delete_car_title),
                    onActionConfirmed = {
                        viewModel.onDeleteCarDataClicked()
                    }
                )
            }
            settingsDeleteServicesOption.root.setOnClickListener {
                showConfirmationDialog(
                    title = getString(R.string.dialog_delete_services_title),
                    onActionConfirmed = {
                        viewModel.onDeleteServicesClicked()
                    }
                )
            }

            settingsAlarmTimeOption.root.setOnClickListener {
                showNumberPickerDialog { numberPicked ->
                    viewModel.onAlarmTimePicked(numberPicked)
                }
            }

            settingsDueDateFormatOption.root.setOnClickListener {
                showDueDateFormatPickerDialog { optionPicked ->
                    viewModel.onDueDateFormatPicked(optionPicked)
                }
            }

            setExportClickListener { createDocumentsContract.launch(generateExportFileName()) }
            setImportClickListener { openDocumentContract.launch(arrayOf(DEFAULT_FILE_MIME_TYPE)) }
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