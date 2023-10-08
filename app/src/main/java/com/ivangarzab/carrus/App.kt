package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.carrus.util.managers.LeakUploader
import com.ivangarzab.carrus.util.managers.Preferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
// Global instance of our shared preferences
val prefs: Preferences by lazy {
    App.preferences!!
} //TODO: Rework this solution

// Global instance of our Application coroutine scope
val appScope: CoroutineScope by lazy {
    App.appScope!!
}

@HiltAndroidApp
open class App : Application() {

    private val appGlobalJob: Job = Job()

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        appScope = CoroutineScope(
            appGlobalJob + Dispatchers.Default
        )
        if (isRelease().not()) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }
    }

    open fun setupLeakCanary() {
        Timber.v("Setting up leak event listener")
        LeakUploader().setupCrashlyticsLeakUploader()
    }

    companion object {
        var preferences: Preferences? = null
        var appScope: CoroutineScope? = null

        fun isRelease(): Boolean = BuildConfig.BUILD_TYPE == "release"
    }
}