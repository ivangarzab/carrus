package com.ivangarzab.carrus.util.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Created by Ivan Garza Bermea.
 */

/**
 * Set a new value for the state from the return value of the block, which is passed
 * to saved state as saved under the given key.
 */
@Suppress("unused")
inline fun <T, S : LiveData<T>> ViewModel.setState(
    state: S,
    savedState: SavedStateHandle,
    key: String,
    block: T.() -> T
) {
    state.value?.let {
        savedState.set(key, block(it))
    } ?: throw IllegalStateException("Have you set an initial value for your state?")
}