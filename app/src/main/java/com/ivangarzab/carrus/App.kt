package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.carrus.data.di.AppModule
import com.ivangarzab.carrus.data.providers.DebugFlagProvider
import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader
import com.ivangarzab.carrus.util.managers.NightThemeManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
open class App : Application() {

    private val debugFlagProvider: DebugFlagProvider by inject()

    open val nightThemeManager: NightThemeManager by inject()

    open val leakUploader: LeakUploader = LeakUploader()

    open val coiler: Coiler = Coiler()

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        nightThemeManager.fetchNightThemeSetting()
        if (debugFlagProvider.isDebugEnabled()) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }
        setupCoil()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            modules(AppModule)
        }
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

