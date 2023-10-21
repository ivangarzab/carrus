package com.ivangarzab.carrus.data.structures

/**
 * Created by Ivan Garza Bermea.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.testing.TestLifecycleOwner
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LiveStateTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testLiveState: LiveState<String>

    private lateinit var owner: TestLifecycleOwner

    private val observer = Observer<String> {
        resultValue = it
    }
    private var resultValue = TEST_DEFAULT_VALUE


    @Before
    fun setup() {
        testLiveState = LiveState(INITIAL_VALUE)
        owner = TestLifecycleOwner()
    }

    @Test
    fun test_LiveState_initialValue_base() {
        val result = testLiveState.getOrAwaitValue()
        assertThat(result)
            .isEqualTo(INITIAL_VALUE)
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

    /*@Test
    fun test_observeForever_base() {
        // Given
        liveEvent.observeForever(observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe multi observers`() {
        // Given
        owner.start()
        val observer1 = mock<Observer<String>>()
        liveEvent.observe(owner, observer)
        liveEvent.observe(owner, observer1)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observeForever multi observers`() {
        // Given
        val observer1 = mock<Observer<String>>()
        liveEvent.observeForever(observer)
        liveEvent.observeForever(observer1)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after start`() {
        // Given
        owner.create()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

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
        liveEvent.value = event
        liveEvent.observe(owner, observer)

        // When
        owner.start()

        // Then
        if (config == LiveEventConfig.Normal) {
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
        liveEvent.observe(owner, observer)
        liveEvent.observe(owner, observer1)

        val event = "event"

        // When
        liveEvent.value = event

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
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after start again`() {
        // Given
        owner.stop()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

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
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

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
        liveEvent.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // Given
        liveEvent.observe(owner1, observer1)

        // Then
        verify(observer1, never()).onChanged(event)

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(2)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after one observation with new owner`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner.destroy()

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        owner = TestLifecycleOwner()
        observer = mock()
        liveEvent.observe(owner, observer)
        owner.start()

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after one observation with new owner after start`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

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
        liveEvent.observe(owner, observer)

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        liveEvent.removeObserver(observer)
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove before emit`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)
        liveEvent.removeObserver(observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove owner`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)

        // When
        liveEvent.removeObservers(owner)
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove owner before emit`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)
        liveEvent.removeObservers(owner)

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, never()).onChanged(event)
    }

    @Test
    fun `observe after remove multi owner`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, never()).onChanged(event)

        // When
        liveEvent.observe(owner1, observer1)
        liveEvent.removeObserver(observer)
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, times(1)).onChanged(event)
    }

    @Test
    fun `observe after remove owner multi owner`() {
        // Given
        owner.start()
        liveEvent.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1 = mock<Observer<String>>()
        owner1.start()

        val event = "event"

        // When
        liveEvent.value = event

        // Then
        verify(observer, times(1)).onChanged(event)
        verify(observer1, never()).onChanged(event)

        // When
        liveEvent.observe(owner1, observer1)
        liveEvent.removeObservers(owner)
        liveEvent.value = event

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
