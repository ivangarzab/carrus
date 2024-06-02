package com.ivangarzab.carrus.data.providers

import android.os.Build

/**
 * Created by Ivan Garza Bermea.
 */
interface BuildVersionProvider {
    fun getSdkVersionInt(): Int
}

class BuildVersionProviderImpl : BuildVersionProvider {
    override fun getSdkVersionInt(): Int {
        return Build.VERSION.SDK_INT
    }
}
