package com.ivangarzab.carbud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
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
        setupWindow()
        setupViews()
        setupBottomSheet()
    }

    private fun setupWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, FULL_SCREEN)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = insets.bottom
                }
            }
            // Return the insets in order for this to keep being passed down to descendant views
            windowInsets
        }
    }

    private fun setupViews() {
        binding.mainActivityNavHost.setOnClickListener { setBottomSheetVisibility(false) }
        binding.mainActivityBottomSheet.root.updatePadding(bottom = PADDING_BOTTOM_SHEET)
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

    fun getBindingRoot() = binding.root

    fun showBottomSheet(onPartCreated: (Part) -> Unit) {
        binding.mainActivityBottomSheet.bind(
            resources = resources,
            onSave = onPartCreated
        )
        setBottomSheetVisibility(true)
    }

    fun hideBottomSheet() = setBottomSheetVisibility(false)

    companion object {
        private const val FULL_SCREEN: Boolean = false
        private const val PADDING_BOTTOM_SHEET: Int = 150
    }
}