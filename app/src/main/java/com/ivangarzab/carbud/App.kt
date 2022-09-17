package com.ivangarzab.carbud

import android.app.Application
import com.ivangarzab.carbud.data.Preferences
import com.ivangarzab.carbud.data.repositories.CarRepository
import com.ivangarzab.carbud.util.AlarmScheduler

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

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        alarmScheduler = AlarmScheduler(applicationContext)
    }

    companion object {
        var preferences: Preferences? = null
        var alarmScheduler: AlarmScheduler? = null
    }
}

//TODO: distribute this thru injection
val carRepository: CarRepository = CarRepository()