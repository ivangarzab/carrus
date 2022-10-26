package com.ivangarzab.carbud.ui.overview

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentOverviewBinding
import com.ivangarzab.carbud.databinding.ModalDetailsBinding
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.delegates.viewBinding
import com.ivangarzab.carbud.util.extensions.setLightStatusBar
import com.ivangarzab.carbud.util.extensions.updateMargins
import timber.log.Timber


/**
 * Created by Ivan Garza Bermea.
 */
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel: OverviewViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val binding: FragmentOverviewBinding by viewBinding()

    private val notificationPermissionRequestLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.toggleNotificationPermissionState(isGranted)
            } else {
                Timber.d("Notification permissions denied")
                // TODO: Implement denial case
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
        setupViews()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.car = state.car
            state.car?.let {
                Timber.d("Got new Car state: ${state.car}")
                setLightStatusBar(false)
                if (state.notificationPermissionState &&
                    it.services.isNotEmpty() &&
                    prefs.isAlarmPastDueActive.not()
                ) {
                    viewModel.schedulePastDueAlarm()
                } else {
                    Timber.v("Alarm is already scheduled")
                }

                binding.overviewContent.apply {
                    overviewContentServiceList.apply {
                        adapter = PartListAdapter(
                            theme = requireContext().theme,
                            services = it.services,
                            onItemClicked = {
                                // TODO: onItemClicked()
                            },
                            onDeleteClicked = { service ->
                                viewModel.onServiceDeleted(service)
                            }
                        )
                    }
                }
            } ?: setLightStatusBar(prefs.darkMode?.not() ?: true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 33) {
            attemptToRequestNotificationPermission()
        } else {
            Timber.v("We don't need Notification permission when sdk=${Build.VERSION.SDK_INT}")
            viewModel.toggleNotificationPermissionState(true)
        }
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(
            (requireActivity() as MainActivity).getBindingRoot()
        ) { _, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                binding.overviewToolbar.updateMargins(
                    top = insets.top
                )
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.apply {
            overviewToolbar.inflateMenu(R.menu.menu_overview)
            overviewToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_details -> {
                        showCarDetailsDialog()
                        true
                    }
                    R.id.action_settings -> {
                        navigateToSettingsFragment()
                        true
                    }
                    R.id.action_add_component -> {
                        navigateToNewServiceBottomSheet()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            overviewAppBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                binding.overviewToolbarLayout.clipToOutline =
                    when (binding.overviewAppBarLayout.totalScrollRange + verticalOffset) {
                        0 -> {
                            showAddServiceMenuOption(true)
                            false
                        }
                        else -> {
                            showAddServiceMenuOption(false)
                            true
                        }
                    }
            }
            overviewContent.overviewContentServiceList.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = RecyclerView.VERTICAL
                }
            }
            setAddCarClickListener { navigateToCreateFragment() }
            setAddComponentClickListener { navigateToNewServiceBottomSheet() }
        }
    }

    private fun showAddServiceMenuOption(visible: Boolean) = binding
        .overviewToolbar
        .menu
        .findItem(R.id.action_add_component)
        .apply {
            isVisible = visible
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun attemptToRequestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED -> {
                notificationPermissionRequestLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
            else -> viewModel.toggleNotificationPermissionState(true)
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
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        bindingDialog.apply {
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

    private fun navigateToCreateFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
    )

    private fun navigateToSettingsFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToSettingsFragment()
    )
}