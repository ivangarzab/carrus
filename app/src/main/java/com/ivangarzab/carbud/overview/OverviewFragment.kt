package com.ivangarzab.carbud.overview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentOverviewBinding
import com.ivangarzab.carbud.delegates.viewBinding

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel: OverviewViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val binding: FragmentOverviewBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        viewModel.fetchDefaultCar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.car = state.car
        }

        binding.setAddCarClickListener {
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
            )
        }
    }
}