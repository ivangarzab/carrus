package com.ivangarzab.carbud.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ivangarzab.carbud.MainActivity
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.databinding.FragmentSettingsBinding
import com.ivangarzab.carbud.util.delegates.viewBinding

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupToolbar()
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(
            (requireActivity() as MainActivity).getBindingRoot()
        ) { _, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                binding.settingsAppBarLayout.apply {
                    updatePadding(top = insets.top)
                }
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.settingsToolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white)
            navigationIcon?.setTint(Color.WHITE)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}