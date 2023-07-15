package com.ivangarzab.carrus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.ivangarzab.carrus.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        /* TODO: Delete once we're fully migrated to Compose
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).let { insets ->
                view.updateMargins(
                    bottom = insets.bottom
                )
            }
            // Return the insets in order for this to keep being passed down to descendant views
            windowInsets
        }*/
    }

    fun getBindingRoot() = binding.root

    companion object {
        const val REQUEST_CODE: Int = 1
        private const val FULL_SCREEN: Boolean = false
    }
}