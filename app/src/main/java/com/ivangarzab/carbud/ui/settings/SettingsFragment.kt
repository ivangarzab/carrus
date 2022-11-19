package com.ivangarzab.carbud.ui.settings

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carbud.BuildConfig
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.data.DueDateFormat
import com.ivangarzab.carbud.databinding.FragmentSettingsBinding
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.delegates.viewBinding
import com.ivangarzab.carbud.util.extensions.toast
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
        setupViews()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            Timber.d("Got new Car state: ${state.car ?: "null"}")
            binding.apply {
                car = state.car
                alarmTime = viewModel.getTimeString(
                    state.alarmTime?.toInt() ?: SettingsViewModel.DEFAULT_ALARM_TIME
                )
                versionNumber = "v${BuildConfig.VERSION_NAME}"
                dueDateFormat = state.dueDateFormat.value
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        prefs.darkMode?.let {
            binding.settingsDarkModeOption.settingsOptionToggle.isChecked = it
        }
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
                    viewModel.onDueDateFormatPicked(DueDateFormat.get(optionPicked))
                }
            }

            setExportClickListener {
                toast("EXPORT")
                viewModel.onExportData()
            }

            setImportClickListener {
                toast("IMPORT")
                viewModel.onImportData()
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
            value = (prefs.alarmPastDueTime ?: SettingsViewModel.DEFAULT_ALARM_TIME) - 1
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
                value = if (options.contains(prefs.dueDateFormat.value)) {
                    options.indexOf(prefs.dueDateFormat.value)
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
}