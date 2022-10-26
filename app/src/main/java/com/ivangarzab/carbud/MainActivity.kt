package com.ivangarzab.carbud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import com.ivangarzab.carbud.databinding.ActivityMainBinding
import com.ivangarzab.carbud.util.extensions.updateMargins

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()
        setDarkMode()
    }

    private fun setupWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, FULL_SCREEN)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                view.updateMargins(
                    bottom = insets.bottom
                )
            }
            // Return the insets in order for this to keep being passed down to descendant views
            windowInsets
        }
    }

    fun getBindingRoot() = binding.root

    private fun setDarkMode() {
        prefs.darkMode?.let {
            when (it) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    companion object {
        const val REQUEST_CODE: Int = 1
        private const val FULL_SCREEN: Boolean = false
    }
}