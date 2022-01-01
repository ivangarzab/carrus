package com.ivangarzab.carbud

import android.app.Application
import com.ivangarzab.carbud.data.Preferences

/**
 * Created by Ivan Garza Bermea.
 */
// Global instance of our shared preferences
val prefs: Preferences by lazy {
    App.preferences!!
}

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
    }

    companion object {
        var preferences: Preferences? = null
    }
}