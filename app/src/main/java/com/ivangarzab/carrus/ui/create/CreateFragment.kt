package com.ivangarzab.carrus.ui.create

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.ivangarzab.carrus.MainActivity
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.databinding.FragmentCreateBinding
import com.ivangarzab.carrus.ui.settings.DEFAULT_FILE_MIME_TYPE
import com.ivangarzab.carrus.util.delegates.viewBinding
import com.ivangarzab.carrus.util.extensions.markRequired
import com.ivangarzab.carrus.util.extensions.navigateBackToOverviewScreen
import com.ivangarzab.carrus.util.extensions.onBackPressed
import com.ivangarzab.carrus.util.extensions.readFromFile
import com.ivangarzab.carrus.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class CreateFragment : Fragment(R.layout.fragment_create) {

    private val viewModel: CreateViewModel by viewModels()

    private val binding: FragmentCreateBinding by viewBinding()

    private val args: CreateFragmentArgs by navArgs()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val openDocumentContract = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        Timber.d("Got result from open document contract: ${uri ?: "<nil>"}")
        uri?.let {
            it.readFromFile(requireContext().contentResolver).let { data ->
                data?.let {
                    viewModel.onImportData(data).let { success ->
                        when (success) {
                            true -> toast("Data imported successfully!")
                            false -> toast("Unable to import data")
                        }
                    }
                } ?: Timber.w("Unable to parse data from file with uri: $uri")
            }
        } ?: Timber.w("Unable to read from file with uri: $uri")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.type = args.data?.let {
            viewModel.onSetupContent(it)
            CreateViewModel.Type.EDIT
        } ?: CreateViewModel.Type.CREATE

        setupWindow()
        setupToolbar()
        setupViews()
        onBackPressed { navigateBackToOverviewScreen() }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                Timber.d("Got image uri: $uri")
                animateImageLayout()
                submitAllData()
                uri.toString().apply {
                    persistUriPermission(this)
                    viewModel.onImageUriReceived(this)
                }
            } ?: Timber.d("No media selected")
        }

        viewModel.apply {
            onSubmit.observe(viewLifecycleOwner) { success ->
                if (success) {
                    Timber.v("Navigating back to Overview fragment")
                    navigateBackToOverviewScreen()
                }
            }
            state.observe(viewLifecycleOwner) {
                it?.let { state ->
                    binding.state = state
                    state.imageUri?.let { uri ->
                        binding.apply {
                            createPreviewImage.setImageURI(Uri.parse(uri))
                        }
                    }
                }
            }
        }
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(
            (requireActivity() as MainActivity).getBindingRoot()
        ) { _, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                binding.createAppBarLayout.updatePadding(top = insets.top)
                binding.root.updatePadding(bottom = insets.bottom)
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.createToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white)
            navigationIcon?.setTint(Color.WHITE)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            inflateMenu(R.menu.menu_create)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_import_data -> {
                        openDocumentContract.launch(
                            arrayOf(DEFAULT_FILE_MIME_TYPE)
                        )
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            markRequiredFields(listOf(
                binding.createMakeInputLayout,
                binding.createModelInputLayout,
                binding.createYearInputLayout
            ))
            createAddPhotoButton.setOnLongClickListener {
                // TODO: EASTER EGG -- Delete before first alpha!
                with(Car.default) {
                    viewModel.apply {
                        onUpdateStateData(
                            nickname, make, model, year, licenseNo, vinNo, tirePressure, totalMiles, milesPerGallon
                        )
                        onSubmitData()
                    }
                }
                true
            }
            setImageClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            setDeleteImageClickListener {
                animateImageLayout()
                viewModel.onImageDeleted()
            }
            setExpandClickListener {
                TransitionManager.beginDelayedTransition(
                    createExpandLayout,
                    AutoTransition().apply {
                        state?.let {
                            if (it.isExpanded) {
                                addTarget(createExpandButton)
                            }
                        }
                    }
                )
                submitAllData()
                viewModel.onExpandToggle()
            }
            setSubmitClickListener {
                viewModel.verifyData(
                    make = binding.createMakeInput.text.toString(),
                    model = binding.createModelInput.text.toString(),
                    year = binding.createYearInput.text.toString()
                ).let {
                    when (it) {
                        false -> toast("Missing required fields")
                        true -> submitAllData()
                            .also { viewModel.onSubmitData() }
                    }
                }
            }
        }
    }

    private fun markRequiredFields(list: List<TextInputLayout>) = list.forEach {
        it.markRequired()
    }

    private fun persistUriPermission(uri: String) {
        requireContext().contentResolver.takePersistableUriPermission(
            Uri.parse(uri),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    private fun animateImageLayout() {
        TransitionManager.beginDelayedTransition(
            binding.createImageLayout,
            AutoTransition().apply {
                duration = TRANSITION_DURATION_IMAGE_UPLOAD
            }
        )
    }

    private fun submitAllData() = with(binding) {
        viewModel.onUpdateStateData(
            nickname = createNicknameInput.text.toString(),
            make = createMakeInput.text.toString(),
            model = createModelInput.text.toString(),
            year = createYearInput.text.toString(),
            licenseNo = createLicenseInput.text.toString(),
            vinNo = createVinNumberInput.text.toString(),
            tirePressure = createTirePressureInput.text.toString(),
            totalMiles = createOdometerInput.text.toString(),
            milesPerGallon = createMiPerGalInput.text.toString()
        )
    }

    companion object {
        private const val TRANSITION_DURATION_IMAGE_UPLOAD = 125L
    }
}