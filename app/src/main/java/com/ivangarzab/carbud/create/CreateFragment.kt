package com.ivangarzab.carbud.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        binding.setSubmitClickListener {
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