package com.ivangarzab.carrus

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache
import com.google.android.libraries.places.api.Places
import com.ivangarzab.carrus.util.managers.LeakUploader
import com.ivangarzab.carrus.util.managers.Preferences
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
// Global instance of our shared preferences
val prefs: Preferences by lazy {
    App.preferences!!
} //TODO: Rework this solution

@HiltAndroidApp
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        setupCoil()
        if (isRelease().not()) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }

        Places.initialize(this, BuildConfig.GOOGLE_MAPS_API_KEY)
    }

    open fun setupLeakCanary() {
        Timber.v("Setting up leak event listener")
        LeakUploader().setupCrashlyticsLeakUploader()
    }

    open fun setupCoil() = with(applicationContext) {
        Coil.setImageLoader(ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .build()
        )
    }

    companion object {
        var preferences: Preferences? = null

        fun isRelease(): Boolean = BuildConfig.BUILD_TYPE == "release"
    }
}