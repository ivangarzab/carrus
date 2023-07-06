package com.ivangarzab.carrus.util.extensions

import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 *
 * Use to log events containing a "π" character meant for testing a specific path.
 */
fun Timber.Forest.t(
    msg: String,
    vararg args: Any?
) {
    d("π: $msg", args)
}

/**
 * Use to log events containing a "π" character as a prefix to an otherwise predefined [source]
 * tag meant for testing a specific path.
 */
fun Timber.Forest.t(
    source: String,
    msg: String,
    vararg args: Any?
) {
    d("π-$source: $msg", args)
}

/**
 * Use to log an analytical event both locally and into the cloud.
 */
fun Timber.Forest.a(
    eventName: String,
    vararg params: Any?
) {
    i("analytics=$eventName", params)
    // TODO: Log analytical event to the cloud
}