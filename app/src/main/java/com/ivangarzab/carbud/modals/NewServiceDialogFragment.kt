package com.ivangarzab.carbud.modals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carbud.databinding.ModalComponentBinding
import com.ivangarzab.carbud.extensions.dismissKeyboard

/**
 * Created by Ivan Garza Bermea.
 */
class NewServiceDialogFragment : BottomSheetDialogFragment() {

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
        binding.root.clipToOutline = true
    }

    override fun onStop() {
        super.onStop()
        dismissKeyboard(binding.root)
    }
}