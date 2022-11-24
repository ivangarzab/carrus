package com.ivangarzab.carrus.util.extensions

import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 *
 * Use to log events containing a "π" character meant for testing a specific path.
 */
fun Timber.t(
    msg: String,
    vararg args: Any?
) {
    Timber.d("π: $msg", args)
}

/**
 * Use to log an analytical event both locally and into the cloud.
 */
fun Timber.a(
    eventName: String,
    vararg params: Any?
) {
    Timber.i("analytics=$eventName", params)
    // TODO: Log analytical event to the cloud
}