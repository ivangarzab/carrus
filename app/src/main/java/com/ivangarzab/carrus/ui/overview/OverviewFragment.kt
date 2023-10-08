package com.ivangarzab.carrus.ui.overview

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.OverviewState
import com.ivangarzab.carrus.util.extensions.areNotificationsEnabled
import com.ivangarzab.carrus.util.extensions.canScheduleExactAlarms
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
                    onServiceEditButtonClicked = { navigateToEditServiceBottomSheet(it) },
                    onSettingsButtonClicked = { navigateToSettingsFragment() },
                    onAddCarClicked = { navigateToCreateFragment() },
                    onMessageClicked = { onMessageClicked(it) }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            processStateChange(it)
        }
    }

    private fun onMessageClicked(id: String) { //TODO: Move into VM
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

    private fun processStateChange(state: OverviewState) { //TODO: Move into VM
        Timber.v("Processing overview state change")
        if (requireContext().canScheduleExactAlarms().not() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            state.hasPromptedForPermissionAlarm.not()
        ) {
            viewModel.addAlarmPermissionMessage()
            Timber.d("Registering alarm permission state changed broadcast receiver")
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
}