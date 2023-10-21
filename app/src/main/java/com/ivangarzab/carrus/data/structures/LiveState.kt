package com.ivangarzab.carrus.data.structures

import android.util.ArraySet
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Created by Ivan Garza Bermea.
 *
 * The purpose of this class is to serve as a handy and easy-to-use extension of Android's
 * [LiveData] class that will allow us to utilize this pattern exclusively using immutable data,
 * while at the same time allowing us to do so with a single variable instead of two.
 *
 * As inspired by: [LiveEvent](https://github.com/hadilq/LiveEvent/blob/main/live-event/src/main/java/com/hadilq/liveevent/LiveEvent.kt)
 */
@Suppress("SENSELESS_COMPARISON") // senseless comparison are needed for testing!
open class LiveState<T>(
    initialValue: T
) : LiveData<T>(initialValue) {

    private val observers = ArraySet<ObserverWrapper<in T>>()
    private var hasValueWithoutFirstObserver: Boolean = true

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (skipDuplicateObservers(observer)) return
        ObserverWrapper(observer).let {
            if (hasValueWithoutFirstObserver) {
                hasValueWithoutFirstObserver = false
                it.newValue()
            }
            observers.add(it)
            super.observe(owner, it)
        }
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        if (skipDuplicateObservers(observer)) return
        ObserverWrapper(observer).let {
            if (hasValueWithoutFirstObserver) {
                hasValueWithoutFirstObserver = false
                it.newValue()
            }
            observers.add(it)
            super.observeForever(it)
        }
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observer is ObserverWrapper && observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        if (observers.isEmpty() || observers.iterator() == null) return

        observers.iterator().let { iterator ->
            while (iterator.hasNext()) {
                iterator.next().let { o ->
                    if (o.observer == observer) {
                        iterator.remove()
                        super.removeObserver(o)
                        return
                    }
                }
            }
        }
    }

    /**
     * Sets a new value of type [T] immediately, and notify all observers.
     */
    @MainThread
    fun setState(block: T.() -> T) {
        if (observers.isEmpty()) hasValueWithoutFirstObserver = true
        getMutableState().let { currentState ->
            observers.forEach { it.newValue() }
            super.setValue(block(currentState))
        }
    }

    /**
     * Queue setting a new value of type [T] for the posterity, and notify all observers.
     */
    @MainThread
    fun postState(block: T.() -> T) {
        getMutableState().let { currentState ->
            observers.forEach { it.newValue() }
            super.postValue(block(currentState))
        }
    }

    private fun getMutableState(): T =
        this.value ?: throw IllegalStateException("LiveState has not been initialized")

    private fun skipDuplicateObservers(observer: Observer<in T>): Boolean {
        return if (observers.isEmpty() || observers.iterator() == null) {
            false
        } else {
            observers
                .find { it.observer == observer }
                ?.let { true }
                ?: false
        }
    }


    /**
     * As acquired from: [LiveEvent](https://github.com/hadilq/LiveEvent/blob/main/live-event/src/main/java/com/hadilq/liveevent/LiveEvent.kt)
     */
    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(value: T) {
            if (pending) {
                pending = false
                observer.onChanged(value)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}