package com.ivangarzab.carrus.ui.overview

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.databinding.ModalDetailsBinding
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.util.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    private val notificationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onNotificationPermissionActivityResult(isGranted)
        if (isGranted.not()) {
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToPermissionNotificationModal()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            AppTheme {
                OverviewScreenStateful(
                    onFloatingActionButtonClicked = { navigateToNewServiceBottomSheet() },
                    onCarEditButtonClicked = { navigateToEditFragment() },
                    onCarDetailsButtonClicked = { showCarDetailsDialog() },
                    onServiceEditButtonClicked = { navigateToEditServiceBottomSheet(it) },
                    onSettingsButtonClicked = { navigateToSettingsFragment() },
                    onAddCarClicked = { navigateToCreateFragment() }
                )
            }
        }
    }

    private fun setupViews() {
        /*binding.apply {
            // Content binding
            overviewContent.apply {
                //TODO: Move into Compose
                overviewMessagesLayout.apply {
                    feedData(viewLifecycleOwner, viewModel.queueState)
                    setOnClickListener { id ->
                        Timber.d("Got a message click with id=$id")
                        when (id) {
                            "100" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                attemptToRequestNotificationPermission()
                            }
                            "101" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                attemptToRequestAlarmsPermission()
                            }
                        }
                    }
                    setOnDismissListener {
                        viewModel.onMessageDismissed()
                    }
                }
            }
        }*/
    }

    private fun processStateChange(state: OverviewState) {
        Timber.v("Processing overview state change")
        if (requireContext().canScheduleExactAlarms().not() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            state.hasPromptedForPermissionAlarm.not()
        ) {
            viewModel.addAlarmPermissionMessage()
            Timber.d("Registering alarm permission state changed broadcast receiver")
            ContextCompat.registerReceiver(
                requireContext(),
                AlarmPermissionStateChangedReceiver(),
                IntentFilter(
                    AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
                ),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        state.car?.let {
            Timber.d("Got new Car state: ${state.car}")
            viewModel.processCarServicesListForNotification(
                services = it.services,
                areNotificationsEnabled = requireContext().areNotificationsEnabled()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun attemptToRequestNotificationPermission() {
        if (requireContext().areNotificationsEnabled().not()) {
            notificationPermissionRequestLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            Timber.v("Notification permission already granted!")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun attemptToRequestAlarmsPermission() {
        if (requireContext().canScheduleExactAlarms().not()) {
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToAlarmPermissionModal()
            )
        } else {
            Timber.v("Alarms permission already granted!")
        }
    }

    private fun showCarDetailsDialog() {
        val car = viewModel.state.value?.car
        car ?: return
        val bindingDialog: ModalDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.modal_details,
            null,
            false
        )
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setView(bindingDialog.detailsModalRoot)
            setCancelable(true)
        }.create().also {
            it.clearBackgroundForRoundedCorners()
        }
        bindingDialog.apply {
            vinNo = car.vinNo
            tirePressure = car.tirePressure
            milesTotal = car.totalMiles
            milesPerGallon = car.milesPerGallon
            detailsModalButton.apply {
                clipToOutline = true
                setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun navigateToNewServiceBottomSheet() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToNewServiceModal()
    )

    private fun navigateToEditServiceBottomSheet(service: Service) = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToNewServiceModal(
            service = service
        )
    )

    private fun navigateToCreateFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
    )

    private fun navigateToEditFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment(
            data = viewModel.state.value?.car
        )
    )

    private fun navigateToSettingsFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToSettingsFragment()
    )

    //TODO: Move into the AlarmSettingsRepository, or add it into its own Repository
    inner class AlarmPermissionStateChangedReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                    Timber.d("Received alarm permission state changed broadcast")
                    viewModel.removeAlarmPermissionMessage()
                    requireContext().unregisterReceiver(this)
                }
            }
        }
    }
}