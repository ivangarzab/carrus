package com.ivangarzab.carbud.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentCreateBinding
import com.ivangarzab.carbud.delegates.viewBinding

/**
 * Created by Ivan Garza Bermea.
 */
class CreateFragment : Fragment(R.layout.fragment_create) {

    private val viewModel: CreateViewModel by viewModels()

    private val binding: FragmentCreateBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setSubmitClickListener {
            Log.d("TAG", "Submit was clicked!")
        }
    }
}