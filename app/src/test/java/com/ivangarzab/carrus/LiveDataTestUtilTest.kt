package com.ivangarzab.carrus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.structures.LiveState
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Ivan Garza Bermea.
 */
class LiveDataTestUtilTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mutableLiveData: MutableLiveData<String>
    private lateinit var liveData: LiveData<String>
    private lateinit var liveState: LiveState<String>

    private fun setupMutableLiveData() {
        mutableLiveData = MutableLiveData(STRING_EMPTY)
    }

    private fun setupLiveData() {
        liveData = mutableLiveData
    }

    private fun setupLiveState() {
        liveState = LiveState(STRING_EMPTY)
    }

    // Test LiveData
    @Test
    fun test_liveData_get_initial_value() {
        setupMutableLiveData()
        setupLiveData()
        val result = liveData.getOrAwaitValue()
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test(expected = TimeoutException::class)
    fun test_liveData_get_no_initial_value() {
        mutableLiveData = MutableLiveData()
        setupLiveData()
        liveData.getOrAwaitValue()
    }

    @Test
    fun test_liveData_await_no_trigger_initial_value() {
        setupMutableLiveData()
        setupLiveData()
        val result = liveData.getOrAwaitValue { }
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test(expected = TimeoutException::class)
    fun test_liveData_await_no_trigger_no_initial_value() {
        mutableLiveData = MutableLiveData()
        setupLiveData()
        liveData.getOrAwaitValue { }
    }

    @Test
    fun test_liveData_await_trigger_new_value() {
        setupMutableLiveData()
        setupLiveData()
        val result = liveData.getOrAwaitValue {
            mutableLiveData.value = STRING_TEST
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    @Test
    fun test_liveData_await_multiple_triggers_first_value() {
        setupMutableLiveData()
        setupLiveData()
        val result = liveData.getOrAwaitValue {
            mutableLiveData.value = STRING_TEST
            mutableLiveData.value = TEST_STRING_1
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    // Test MutableLiveData
    @Test
    fun test_mutableLiveData_get_initial_value() {
        setupMutableLiveData()
        val result = mutableLiveData.getOrAwaitValue()
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test(expected = TimeoutException::class)
    fun test_mutableLiveData_get_no_initial_value() {
        mutableLiveData = MutableLiveData()
        mutableLiveData.getOrAwaitValue()
    }

    @Test
    fun test_mutableLiveData_await_no_trigger_initial_value() {
        setupMutableLiveData()
        val result = mutableLiveData.getOrAwaitValue { }
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test(expected = TimeoutException::class)
    fun test_mutableLiveData_await_no_trigger_no_initial_value() {
        mutableLiveData = MutableLiveData()
        mutableLiveData.getOrAwaitValue { }
    }

    @Test
    fun test_mutableLiveData_await_trigger_new_value() {
        setupMutableLiveData()
        val result = mutableLiveData.getOrAwaitValue {
            mutableLiveData.value = STRING_TEST
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    @Test
    fun test_mutableLiveData_await_multiple_triggers_first_value() {
        setupMutableLiveData()
        val result = mutableLiveData.getOrAwaitValue {
            mutableLiveData.value = STRING_TEST
            mutableLiveData.value = TEST_STRING_1
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    // Test LiveState
    @Test
    fun test_liveState_get_initial_value() {
        setupLiveState()
        val result = liveState.getOrAwaitValue()
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test
    fun test_liveState_await_no_trigger_initial_value() {
        setupLiveState()
        val result = liveState.getOrAwaitValue { }
        assertThat(result)
            .isEqualTo(STRING_EMPTY)
    }

    @Test
    fun test_liveState_await_trigger_new_value() {
        setupLiveState()
        val result = liveState.getOrAwaitValue {
            liveState.setState { STRING_TEST }
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    @Test
    fun test_liveState_await_multiple_triggers_first_value() {
        setupLiveState()
        val result = liveState.getOrAwaitValue {
            liveState.setState { STRING_TEST }
            liveState.setState { TEST_STRING_1 }
        }
        assertThat(result)
            .isEqualTo(STRING_TEST)
    }

    companion object {
        private const val TEST_STRING_1 = "test1"
    }
}