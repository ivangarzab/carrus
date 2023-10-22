package com.ivangarzab.carrus.data.structures

/**
 * Created by Ivan Garza Bermea.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.testing.TestLifecycleOwner
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class LiveStateTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testLiveState: LiveState<String>

    private lateinit var owner: TestLifecycleOwner

    private var resultValue = TEST_DEFAULT_VALUE
    private val observer = Observer<String> {
        resultValue = it
    }

    @Before
    fun setup() {
        testLiveState = LiveState(INITIAL_VALUE)
        owner = TestLifecycleOwner()
    }

    @Test
    fun test_observe_base() {
        testLiveState.observe(owner, observer)
        assertThat(resultValue)
            .isEqualTo(INITIAL_VALUE)
    }

    @Test
    fun test_observe_setState() {
        testLiveState.observe(owner, observer)
        testLiveState.setState { TEST_VALUE }
        assertThat(resultValue)
            .isEqualTo(TEST_VALUE)
    }

    @Test
    fun test_observeForever_base() {
        testLiveState.observeForever(observer)
        assertThat(resultValue)
            .isEqualTo(INITIAL_VALUE)
    }
    @Test
    fun `observe multi observers`() {
//        val observer1 = mock(Observer<String>)
        testLiveState.observe(owner, observer)
        testLiveState.observe(owner, observer1)
        testLiveState.setState { TEST_VALUE }

//        assertThat(result).isEqualTo(TEST_VALUE)
        verify(observer, times(1)).onChanged(TEST_VALUE)
        verify(observer1, times(1)).onChanged(TEST_VALUE)
    }

/*
    @Test
    fun `observeForever multi observers`() {
        // Given
        val observer1 = mock<Observer<String>>()
        testLiveState.observeForever(observer)
        testLiveState.observeForever(observer1)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after start`() {
        // Given
        owner.create()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)

        // When
        owner.start()

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after emit event`() {
        // Given
        owner.create()
        val event = "event"
        testLiveState.value = event
        testLiveState.observe(owner, observer)

        // When
        owner.start()

        // Then
        if (config == testLiveStateConfig.Normal) {
            verify(observer, never()).onChanged(event)
        } else {
            verify(observer, times(1)).onChanged(event)
        }
    }

    @Test
    fun `observe after start with multi observers`() {
        // Given
        owner.create()
        val observer1 = mock<Observer<String>>()
        testLiveState.observe(owner, observer)
        testLiveState.observe(owner, observer1)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)
        verify(observer1, never()).onChanged(event)

        // When
        owner.start()

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after stop`() {
        // Given
        owner.stop()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after start again`() {
        // Given
        owner.stop()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)

        // When
        owner.start()

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after one observation`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner.stop()

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner.start()

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after one observation multi owner`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // Given
        testLiveState.observe(owner1, observer1)

        // Then
        verify(observer1, never()).onChanged(event)

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(2)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after one observation with new owner`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner.destroy()

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner = TestLifecycleOwner()
        observer = mock()
        testLiveState.observe(owner, observer)
        owner.start()

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after one observation with new owner after start`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner.destroy()

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner = TestLifecycleOwner()
        observer = mock()
        owner.start()
        testLiveState.observe(owner, observer)

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        testLiveState.removeObserver(observer)
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove before emit`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)
        testLiveState.removeObserver(observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove owner`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        testLiveState.removeObservers(owner)
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove owner before emit`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)
        testLiveState.removeObservers(owner)

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove multi owner`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, never()).onChanged(event)

        // When
        testLiveState.observe(owner1, observer1)
        testLiveState.removeObserver(observer)
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove owner multi owner`() {
        // Given
        owner.start()
        testLiveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, never()).onChanged(event)

        // When
        testLiveState.observe(owner1, observer1)
        testLiveState.removeObservers(owner)
        testLiveState.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }*/

    companion object {
        private const val TEST_DEFAULT_VALUE = ""
        private const val INITIAL_VALUE = "initialValue"
        private const val TEST_VALUE = "test-value"
    }
}
