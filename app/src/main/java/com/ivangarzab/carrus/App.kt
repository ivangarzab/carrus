package com.ivangarzab.carrus

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache
import com.ivangarzab.carrus.data.di.DebugFlagProvider
import com.ivangarzab.carrus.util.managers.LeakUploader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ivan Garza Bermea.
 */
@HiltAndroidApp
open class App : Application() {

    @Inject
    lateinit var debugFlagProvider: DebugFlagProvider

    override fun onCreate() {
        super.onCreate()
        if (debugFlagProvider.isDebugEnabled()) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }
        setupCoil()
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
}