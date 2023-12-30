package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
@HiltAndroidApp
open class App : Application() {

    open val leakUploader: LeakUploader = LeakUploader()

    open val coiler: Coiler = Coiler()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }
        setupCoil()
        /*TODO: Not needed + will get deleted with maps_compose
        Places.initialize(this, BuildConfig.GOOGLE_MAPS_API_KEY)*/
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

