package com.ivangarzab.carbud

import android.app.Application
import com.ivangarzab.carbud.data.Preferences
import com.ivangarzab.carbud.data.repositories.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Created by Ivan Garza Bermea.
 */
// Global instance of our shared preferences
val prefs: Preferences by lazy {
    App.preferences!!
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
        appScope = CoroutineScope(
            appGlobalJob + Dispatchers.Default
        )

    }

    companion object {
        var preferences: Preferences? = null
        var appScope: CoroutineScope? = null
    }
}

//TODO: distribute this thru injection
val carRepository: CarRepository = CarRepository()