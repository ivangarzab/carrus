package com.ivangarzab.carrus.util.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Created by Ivan Garza Bermea.
 */

/**
 * Set a new value for the state from the return value of the block, which is passed
 * to saved state as saved under the given key.
 */
@Deprecated("Use LiveState class instead",
    ReplaceWith("LiveState.setState(..)")
)
inline fun <T, S : LiveData<T>> ViewModel.setState(
    state: S,
    savedState: SavedStateHandle,
    key: String,
    block: T.() -> T
) {
    state.value?.let {
        savedState[key] = block(it)
    } ?: throw IllegalStateException("Have you set an initial value for your state?")
}

/**
 * Set a new value into the [MutableLiveData] state, by using the [LiveData] state as a base.
 */
@Deprecated("Use LiveState class instead",
    ReplaceWith("LiveState.setState(..)")
)
fun <T, M: MutableLiveData<T>, S: LiveData<T>> ViewModel.setState(
    state: S,
    mutableState: M,
    block: T.() -> T
) {
    state.value?.let {
        mutableState.value = block(it)
    }
}