package com.ivangarzab.carrus

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by Ivan Garza Bermea.
 */

/**
 * The following function is meant to be used in order to easily and quickly get data from
 * [LiveData] instances.  This function will take 1 out of the following 3 paths:
 *  1.  If there's already a value, and no trigger defined, get that value
 *  2.  If there's a trigger defined, wait for the trigger and attempt to get result afterwards
 *  3.  If there's a trigger defined, wait for trigger.  If no new value is set, grab the current value, if available.
 *
 * Gets the current value of a [LiveData] or waits for it to have one, with a timeout.
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 *
 * As acquired from: https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-basics/#8
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    trigger: (() -> Unit)? = null
): T {
    var result: T? = null
    val latch = CountDownLatch(1)
    var triggered = false

    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            // Ignore default/initial values if we're explicitly waiting for a trigger invocation
            if (trigger != null && triggered.not()) {
                return
            }
            result = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    // This call will consume any default/initial values right away
    this.observeForever(observer)
    try {
        triggered = true
        trigger?.invoke()
        // Don't wait indefinitely if the LiveData is not set
        if (!latch.await(time, timeUnit)) {
            this.value?.let {
                return it
            } ?: throw TimeoutException("LiveData value didn't arrive in time.")
        }
    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return result as T
}