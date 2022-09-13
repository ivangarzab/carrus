package com.ivangarzab.carbud.create

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentCreateBinding
import com.ivangarzab.carbud.delegates.viewBinding
import com.ivangarzab.carbud.extensions.toast

/**
 * Created by Ivan Garza Bermea.
 */
class CreateFragment : Fragment(R.layout.fragment_create) {

    private val viewModel: CreateViewModel by viewModels()

    private val binding: FragmentCreateBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
        setupViews()
        viewModel.onSubmit.observe(viewLifecycleOwner) { success ->
            if (success) findNavController().popBackStack()
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
        }
    }

    private fun setupViews() {
        binding.apply {

            setDocumentsClickListener { toast("Coming Soon!") }
            setSubmitClickListener {
                viewModel.verifyData(
                    make = binding.createMakeInput.text.toString(),
                    model = binding.createModelInput.text.toString(),
                    year = binding.createYearInput.text.toString()
                ).let {
                    when (it) {
                        false -> toast("Missing required fields")
                        true -> viewModel.submitData(
                            nickname = binding.createNicknameInput.text.toString(),
                            make = binding.createMakeInput.text.toString(),
                            model = binding.createModelInput.text.toString(),
                            year = binding.createYearInput.text.toString(),
                            licenseNo = binding.createLicenseInput.text.toString()
                        )
                    }
                }
            }
        }
    }
}