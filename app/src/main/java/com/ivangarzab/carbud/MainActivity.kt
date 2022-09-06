package com.ivangarzab.carbud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ivangarzab.carbud.data.Part
import com.ivangarzab.carbud.databinding.ActivityMainBinding
import com.ivangarzab.carbud.extensions.bind

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupBottomSheet()
    }

    private fun setupViews() {
        binding.mainActivityNavHost.setOnClickListener { setBottomSheetVisibility(false) }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(
            binding.mainActivityBottomSheet.componentModalRoot
        )
        setBottomSheetVisibility(false)
    }

    private fun setBottomSheetVisibility(isVisible: Boolean) {
        bottomSheetBehavior.state = when (isVisible) {
            true -> BottomSheetBehavior.STATE_EXPANDED
            false -> BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun showBottomSheet(onPartCreated: (Part) -> Unit) {
        binding.mainActivityBottomSheet.bind(
            resources = resources,
            onSave = onPartCreated
        )
        setBottomSheetVisibility(true)
    }

    fun hideBottomSheet() = setBottomSheetVisibility(false)
}