package com.ivangarzab.carbud.modals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.databinding.ModalComponentBinding
import com.ivangarzab.carbud.extensions.dismissKeyboard
import com.ivangarzab.carbud.extensions.toast
import com.ivangarzab.carbud.overview.OverviewViewModel
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class NewServiceDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: OverviewViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private lateinit var binding: ModalComponentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModalComponentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setSaveClickListener {
                val name = componentModalNameField.text.toString()
                when (viewModel.verifyServiceData(
                    name = name
                )) {
                    true -> {
                        viewModel.onServiceCreated(
                            Service(
                                name = name,
                                lastDate = Calendar.getInstance(), // TODO: Is today a fair assumption to start with?
                                dueDate = Calendar.getInstance().apply { timeInMillis = 1667269800000 } // TODO: Hardcoded
                            )
                        )
                        this@NewServiceDialogFragment.dismiss()
                    }
                    false -> toast("Missing required data")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dismissKeyboard(binding.root)
    }
}