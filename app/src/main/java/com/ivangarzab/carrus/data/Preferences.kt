package com.ivangarzab.carrus.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.google.gson.Gson
import timber.log.Timber
import java.util.*

/**
 * Should only be accessed by Repository, or other data handling classes.
 *
 * Created by Ivan Garza Bermea.
 */
@Suppress("ReplaceGetOrSet")
class Preferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        DEFAULT_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    var darkMode: Boolean?
        get() = when (sharedPreferences.contains(KEY_DARK_MODE)) {
            true -> sharedPreferences.get(KEY_DARK_MODE, false)
            false -> null
        }
        set(value) = sharedPreferences.set(KEY_DARK_MODE, value)

    var defaultCar: Car?
        get() = when (sharedPreferences.contains(KEY_DEFAULT_CAR)) {
            true -> sharedPreferences.get(KEY_DEFAULT_CAR, Car.empty)
            false -> null
        }
        set(value) = sharedPreferences.set(KEY_DEFAULT_CAR, value)
    fun addService(service: Service) {
        defaultCar = defaultCar?.apply {
            services = services.toMutableList().apply { add(service) }
        }
    }
    fun deleteService(service: Service) {
        defaultCar = defaultCar?.apply {
            services = services.toMutableList().apply { remove(service) }
        }
    }

    var isAlarmPastDueActive: Boolean
        get() = sharedPreferences.get(KEY_ALARM_PAST_DUE_INTENT, false)
        set(value) = sharedPreferences.set(KEY_ALARM_PAST_DUE_INTENT, value)

    var alarmPastDueTime: Int?
        get() = sharedPreferences.get(KEY_ALARM_PAST_DUE_TIME, -1).let {
            when (it) {
                in 1..24 -> it
                else -> null
            }
        }
        set(value) = sharedPreferences.set(KEY_ALARM_PAST_DUE_TIME, value)

    var dueDateFormat: DueDateFormat
        get() = sharedPreferences.get(KEY_FORMAT_DUE_DATE, DueDateFormat.DAYS.name).let {
            DueDateFormat.get(it)
        }
        set(value) = sharedPreferences.set(KEY_FORMAT_DUE_DATE, value)

    init {
        defaultCar?.let { car ->
            Timber.d("Default car from a past version: $car")
            if (car.services.isNotEmpty() && car.services.get(0).version != VERSION_SERVICE) {
                // Outdated data -- update on the background
                defaultCar = car.copy(
                    services = car.services.map {
                        Service(
                            id = UUID.randomUUID().toString(),
                            name = it.name,
                            repairDate = it.repairDate,
                            dueDate = it.dueDate
                        )
                    }
                )
            }
        }

        // Get Night Mode state on first install
        if (darkMode == null) {
            val nightModeFlags: Int = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            darkMode = when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
            Timber.i("Dark Mode set to: $darkMode")
        }
    }

    companion object {
        private const val DEFAULT_SHARED_PREFS = "com.ivangarzab.carbud.preferences"
        private const val KEY_DARK_MODE = "dark-mode"
        private const val KEY_DEFAULT_CAR = "default-car"
        private const val KEY_ALARM_PAST_DUE_INTENT = "alarm-past-due-intent"
        private const val KEY_ALARM_PAST_DUE_TIME = "alarm-past-due-time-hour"
        private const val KEY_FORMAT_DUE_DATE = "format-due-date"
    }
}

// SharedPreferences extensions
/**
 * Overwrites the [SharedPreferences.edit] extension function to facilitate its usage.
 */
inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

/**
 * Puts a [value] for the given [key].
 */
operator fun SharedPreferences.set(
    key: String,
    value: Any?
) = when (value) {
    is String? -> edit { it.putString(key, value) }
    is Int -> edit { it.putInt(key, value) }
    is Boolean -> edit { it.putBoolean(key, value) }
    is Float -> edit { it.putFloat(key, value) }
    is Long -> edit { it.putLong(key, value) }
    is Car -> edit { it.putString(key, value.toJson()) }
    is DueDateFormat -> edit { it.putString(key, value.value)}
    else -> throw UnsupportedOperationException("Only native types are supported")
}

/**
 * Finds a preference based on the given [key], where [T] is the type of value.
 *
 * @param defaultValue optional default value - will assume a default defaultValue if it is not specified
 */
inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T = when (T::class) {
    String::class -> getString(key, defaultValue as? String ?: "") as T
    Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
    Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
    Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
    Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
    Car::class -> getString(key, defaultValue as? String ?: "").let {
        (Gson().fromJson(it, Car::class.java) ?: Car.empty) as T
    }
    DueDateFormat::class -> getString(key, defaultValue as String).let {
        DueDateFormat.get(it ?: DueDateFormat.DAYS.name) as T
    }
    else -> throw UnsupportedOperationException("Only native types are supported")
}