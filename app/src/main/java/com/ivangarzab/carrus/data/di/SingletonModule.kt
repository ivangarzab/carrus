package com.ivangarzab.carrus.data.di

/**
 * Created by Ivan Garza Bermea.
 */
open class SingletonModule<T> {
    protected var instance: T? = null

    fun checkForInstanceAndCreateAsNeeded(classType: Class<T>) {
        if (instance == null) {
            createInstance(classType)
        }
    }

    private fun createInstance(classType: Class<T>) {
        instance = classType.getDeclaredConstructor().newInstance()
    }
}