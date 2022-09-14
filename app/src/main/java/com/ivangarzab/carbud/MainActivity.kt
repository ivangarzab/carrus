package com.ivangarzab.carbud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import com.ivangarzab.carbud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()
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

    fun getBindingRoot() = binding.root

    companion object {
        private const val FULL_SCREEN: Boolean = false
    }
}