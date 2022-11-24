package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.carrus.data.Preferences
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.util.AlarmScheduler
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
}

val alarms: AlarmScheduler by lazy {
    App.alarmScheduler!!
}

// Global instance of our Application coroutine Scope -- this should replace the use of GlobalScope
val appScope: CoroutineScope by lazy {
    App.appScope!!
}

open class App : Application() {

    private val appGlobalJob: Job = Job()

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        alarmScheduler = AlarmScheduler(applicationContext)
        appScope = CoroutineScope(
            appGlobalJob + Dispatchers.Default
        )
        if (BuildConfig.BUILD_TYPE != "release") {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
        }
    }

    companion object {
        var preferences: Preferences? = null
        var appScope: CoroutineScope? = null
        var alarmScheduler: AlarmScheduler? = null
    }
}

//TODO: distribute this thru injection
val carRepository: CarRepository = CarRepository()