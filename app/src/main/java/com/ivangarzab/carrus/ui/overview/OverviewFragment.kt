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
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.util.extensions.areNotificationsEnabled
import com.ivangarzab.carrus.util.extensions.canScheduleExactAlarms
import com.ivangarzab.carrus.util.managers.Analytics
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * Created by Ivan Garza Bermea.
 */
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModel()

    val analytics: Analytics by inject()

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
                    onCarEditButtonClicked = {
                        navigateToEditFragment()
                        analytics.logEditCarClicked()
                    },
                    onSettingsButtonClicked = {
                        navigateToSettingsFragment()
                        analytics.logSettingsClicked()
                    },
                    onAddCarClicked = {
                        navigateToCreateFragment()
                        analytics.logAddNewCarClicked()
                    },
                    onMapButtonClicked = {
                        navigateToMapFragment()
                        analytics.logMapClicked()
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            //TODO: Move into Compose
            servicePanelState.observe(viewLifecycleOwner) {
                processServiceDataChange(
                    it.serviceItemList.isEmpty(),
                    requireContext().areNotificationsEnabled()
                )
                checkForAlarmPermission(requireContext().canScheduleExactAlarms())
            }

            triggerNotificationPermissionRequest.observe(viewLifecycleOwner) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    attemptToRequestNotificationPermission()
                }
            }
            triggerAlarmsPermissionRequest.observe(viewLifecycleOwner) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    attemptToRequestAlarmsPermission()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logOverviewScreenView(this@OverviewFragment::class.java.simpleName)
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

    private fun navigateToCreateFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
    )

    private fun navigateToEditFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment(
            // We're sort of assuming the data is there...
            data = viewModel.carDataInternal
        )
    )

    private fun navigateToSettingsFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToSettingsFragment()
    )

    private fun navigateToMapFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToMapFragment()
    )
}