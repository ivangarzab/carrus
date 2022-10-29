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
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentSettingsBinding
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.delegates.viewBinding
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
                    state.alarmTime?.toInt() ?: DEFAULT_ALARM_TIME
                )
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
        }
    }

    private fun showNumberPickerDialog(
        onNumberPicked: (String) -> Unit
    ) {
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 23
            displayedValues = arrayOf(
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
            )
            value = (prefs.alarmPastDueTime ?: DEFAULT_ALARM_TIME) - 1
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
        private const val DEFAULT_ALARM_TIME: Int = 7
    }
}