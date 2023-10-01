package com.ivangarzab.carrus

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var alarmSettingsRepository: AlarmSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        //TODO: Migrate the next 3 lines into Compose
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Listen for alarm permission changes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmSettingsRepository.listenForAlarmPermissionChanges(applicationContext)
        }
    }

    companion object {
        const val REQUEST_CODE: Int = 1
    }
}