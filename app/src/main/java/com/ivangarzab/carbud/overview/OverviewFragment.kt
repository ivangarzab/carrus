package com.ivangarzab.carbud.overview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentOverviewBinding
import com.ivangarzab.carbud.delegates.viewBinding
import com.ivangarzab.carbud.extensions.dismissKeyboard
import com.ivangarzab.carbud.extensions.hideBottomSheet
import com.ivangarzab.carbud.extensions.showBottomSheet


/**
 * Created by Ivan Garza Bermea.
 */
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel: OverviewViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val binding: FragmentOverviewBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.d("IGB", "Got new Car state: ${state.car}")
            binding.car = state.car
            state.car?.let {
                binding.overviewComponentList.apply {
                    adapter = PartListAdapter(
                        parts = it.parts,
                        onItemClicked = {
                            // TODO: onItemClicked()
                        },
                        onEditClicked = {
                            // TODO: onEditClicked()
                        }
                    )
                }
            }

        }

        binding.apply {
            overviewTopImage.clipToOutline = true
            overviewComponentList.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = RecyclerView.VERTICAL
                }
            }
            setAddCarClickListener { navigateToCreateFragment() }
            setAddComponentClickListener {
                showBottomSheet {
                    dismissKeyboard(binding.root)
                    viewModel.onNewPartCreated(it)
                    // Got to give the keyboard a little bit of time to hide.. TODO: Fix
                    hideBottomSheet()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchDefaultCar()
    }

    private fun navigateToCreateFragment() = findNavController().navigate(
        OverviewFragmentDirections.actionOverviewFragmentToCreateFragment()
    )
}