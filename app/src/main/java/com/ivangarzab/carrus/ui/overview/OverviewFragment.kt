package com.ivangarzab.carrus.ui.overview

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivangarzab.carrus.MainActivity
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.databinding.FragmentOverviewBinding
import com.ivangarzab.carrus.databinding.ModalDetailsBinding
import com.ivangarzab.carrus.prefs
import com.ivangarzab.carrus.util.delegates.viewBinding
import com.ivangarzab.carrus.util.extensions.areNotificationsEnabled
import com.ivangarzab.carrus.util.extensions.clearBackgroundForRoundedCorners
import com.ivangarzab.carrus.util.extensions.setLightStatusBar
import com.ivangarzab.carrus.util.extensions.updateMargins
import timber.log.Timber


/**
 * Created by Ivan Garza Bermea.
 */
class OverviewFragment : Fragment(R.layout.fragment_overview), SortingCallback {

    private val viewModel: OverviewViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val binding: FragmentOverviewBinding by viewBinding()

    private var serviceListAdapter: ServiceListAdapter? = null

    private var hasNotificationPermissionPrompted = false
    private val notificationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermissionPrompted = true
        viewModel.onPermissionActivityResult(isGranted)
        if (isGranted.not()) findNavController().navigate(
            OverviewFragmentDirections.actionOverviewFragmentToPermissionNotificationModal()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
        setupViews()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            processStateChange(state)
        }

        binding.overviewToolbarImage.setOnLongClickListener {
            viewModel.addTestMessage()
            true
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
                binding.overviewContent.overviewContentServiceList.updateMargins(
                    bottom = insets.bottom
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
                    R.id.action_edit -> {
                        navigateToEditFragment()
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
        setupServicesList()
        binding.apply {
            overviewAppBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                binding.overviewToolbarLayout.clipToOutline =
                    when (binding.overviewAppBarLayout.totalScrollRange + verticalOffset) {
                        0 -> {
                            showAddServiceMenuOption(true)
                            false
                        }
                        else -> {
                            showAddServiceMenuOption(
                                binding.overviewAddFab.isVisible.not()
                            )
                            true
                        }
                    }
            }
            setAddCarClickListener { navigateToCreateFragment() }
            setAddComponentClickListener { navigateToNewServiceBottomSheet() }

            // Content binding
            overviewContent.apply {
                sortingCallback = this@OverviewFragment
                overviewContentServiceList.apply {
                    // Set up recycler view
                    layoutManager = LinearLayoutManager(requireContext()).apply {
                        orientation = RecyclerView.VERTICAL
                    }
                }
                // EASTER EGG: Test Car Service data
                overviewServicesLabel.setOnLongClickListener {
                    viewModel.setupEasterEggForTesting()
                    true
                }
                overviewMessagesLayout.apply {
                    feedData(viewLifecycleOwner, viewModel.queueState)
                    setOnClickListener { id ->
                        Timber.d("Got a message click with id=$id")
                        when (id) {
                            "100" -> if (Build.VERSION.SDK_INT >= 33) {
                                attemptToRequestNotificationPermission()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupServicesList() {
        serviceListAdapter = ServiceListAdapter(
            resources = requireContext().resources,
            theme = requireContext().theme,
            services = emptyList()
        ).apply {
            setOnEditClickedListener { service ->
                navigateToEditServiceBottomSheet(service)
            }
            setOnDeleteClickedListener { service ->
                viewModel.onServiceDeleted(service)
            }
        }
        binding.overviewContent.overviewContentServiceList.apply {
                adapter = serviceListAdapter
        }
    }

    private fun processStateChange(state: OverviewViewModel.OverviewState) {
        binding.overviewContent.apply {
            when (state.serviceSortingType) {
                SortingCallback.SortingType.NONE -> onSortingViews(
                    overviewServiceSortNoneCard,
                    overviewServiceSortNoneLabel
                )
                SortingCallback.SortingType.NAME -> onSortingViews(
                    overviewServiceSortNameCard,
                    overviewServiceSortNameLabel
                )
                SortingCallback.SortingType.DATE -> onSortingViews(
                    overviewServiceSortDateCard,
                    overviewServiceSortDateLabel
                )
            }
        }

        binding.car = state.car
        state.car?.let {
            Timber.d("Got new Car state: ${state.car}")
            setLightStatusBar(false)
            if (it.services.isNotEmpty()) {
                when (requireContext().areNotificationsEnabled()) {
                    true -> {
                        if (prefs.isAlarmPastDueActive.not()) {
                            // TODO: This should be moved into the VM
                            viewModel.schedulePastDueAlarm()
                        } else {
                            Timber.v("No need to schedule 'Past Due' alarm")
                        }
                    }
                    false -> {
                        if (Build.VERSION.SDK_INT >= 33 && hasNotificationPermissionPrompted.not()) {
                            viewModel.addNotificationPermissionMessage()
                        } else {
                            Timber.v("We don't need Notification permission for sdk=${Build.VERSION.SDK_INT} (<33)")
                        }
                    }
                }
            }

            serviceListAdapter?.updateContent(it.services)

            binding.overviewAppBarLayout.apply {
                layoutParams = CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        when (it.imageUri == null) {
                            true -> SIZE_TOP_VIEW_PICTURELESS
                            false -> SIZE_TOP_VIEW_PICTUREFULL
                        },
                        resources.displayMetrics
                    ).toInt()
                )
            }
            it.imageUri?.let { uri ->
                try {
                    binding.overviewToolbarImage.setImageURI(Uri.parse(uri))
                } catch (e: Exception) {
                    // Make sure we don't crash if there are any problems accessing the image file
                    Timber.w("Caught exception while parsing imageUrl", e)
                }
            }
        } ?: setLightStatusBar(prefs.darkMode?.not() ?: true)
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
        if (requireContext().areNotificationsEnabled().not()) {
            notificationPermissionRequestLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            Timber.v("Notification permission already granted!")
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

    override fun onSort(type: SortingCallback.SortingType) {
        Timber.v("Got a sorting request with type=$type")
        viewModel.onSortingByType(type)
    }

    private fun onSortingViews(current: View, label: TextView) {
        processSortingViews(current)
        highlightSelectedSortingView(current, label)
    }

    private fun highlightSelectedSortingView(
        view: View,
        label: TextView
    ) = ViewCompat.setBackgroundTintList(
        view,
        ContextCompat.getColorStateList(
            requireContext(),
            R.color.indigo
        )
    ).also {
        // Switch color, if needed
       prefs.darkMode?.let { darkMode ->
           if (darkMode.not()) {
               label.setTextColor(
                   requireContext().getColor(R.color.white)
               )
           }
       }
    }

    private fun processSortingViews(
        current: View
    ) = binding.overviewContent.apply {
        listOf(
            overviewServiceSortNoneCard,
            overviewServiceSortNameCard,
            overviewServiceSortDateCard
        ).forEach { target ->
            if (target != current) {
                unhighlightSortingView(target)
            }
        }
        // if needed, return text color to normal
        prefs.darkMode?.let { darkMode ->
            if (darkMode.not()) {
                listOf(
                    overviewServiceSortNoneLabel,
                    overviewServiceSortNameLabel,
                    overviewServiceSortDateLabel
                ).forEach {
                    it.setTextColor(
                        requireContext().getColor(R.color.black)
                    )
                }
            }
        }
    }

    private fun unhighlightSortingView(
        view: View
    ) = ViewCompat.setBackgroundTintList(
        view,
        ContextCompat.getColorStateList(
            requireContext(),
            R.color.background
        )
    )

    companion object {
        private const val SIZE_TOP_VIEW_PICTUREFULL: Float = 260f
        private const val SIZE_TOP_VIEW_PICTURELESS: Float = 170f
    }
}
