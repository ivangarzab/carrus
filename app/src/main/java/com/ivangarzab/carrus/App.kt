package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.carrus.data.di.DebugFlagProvider
import com.ivangarzab.carrus.util.managers.Coiler
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

    open val leakUploader: LeakUploader = LeakUploader()

    open val coiler: Coiler = Coiler()

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
        leakUploader.setupCrashlyticsLeakUploader()
    }

    open fun setupCoil() {
        Timber.v("Setting up Coil")
        coiler.setImageLoader(this)
    }
}

