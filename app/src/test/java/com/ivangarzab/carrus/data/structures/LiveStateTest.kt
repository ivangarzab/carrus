package com.ivangarzab.carrus.data.structures

/**
 * Created by Ivan Garza Bermea.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LiveStateTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testLiveState: LiveState<String>

    @Before
    fun setup() {
        testLiveState = LiveState(INITIAL_VALUE)
    }

    @Test
    fun test_LiveState_initialValue_base() {
        val result = testLiveState.getOrAwaitValue()
        assertThat(result)
            .isEqualTo(INITIAL_VALUE)
    }

    companion object {
        private const val INITIAL_VALUE = "initialValue"
    }
}
