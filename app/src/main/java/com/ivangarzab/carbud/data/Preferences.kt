package com.ivangarzab.carbud.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * Created by Ivan Garza Bermea.
 */
class Preferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        DEFAULT_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

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

    companion object {
        private const val DEFAULT_SHARED_PREFS = "com.ivangarzab.carbud.preferences"
        private const val KEY_DEFAULT_CAR = "default-car"
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
        Gson().fromJson(it, Car::class.java) as T
    }
    else -> throw UnsupportedOperationException("Only native types are supported")
}