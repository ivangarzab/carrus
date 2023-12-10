package com.ivangarzab.carrus.data.structures

/**
 * Created by Ivan Garza Bermea.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ivangarzab.test_data.TestLifecycleOwner
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LiveStateTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var liveState: LiveState<String>

    private lateinit var owner: TestLifecycleOwner

    private lateinit var observer: Observer<String>

    @Before
    fun setup() {
        liveState = LiveState(INITIAL_VALUE)
        owner = TestLifecycleOwner()
        observer = spyk()
    }

    @Test
    fun test_observe_base() {
        liveState.observe(owner, observer)
        verify(atLeast = 0) {
            observer.onChanged(TEST_VALUE)
        }
    }

    @Test
    fun test_observeForever_base() {
        liveState.observeForever(observer)
        verify(atLeast = 0) {
            observer.onChanged(TEST_VALUE)
        }
    }

    @Test
    fun `observe multi observers`() {
        owner.start()
        val observer1: Observer<String> = spyk()
        liveState.observe(owner, observer)
        liveState.observe(owner, observer1)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe multi observers with post`() {
        owner.start()
        val observer1: Observer<String> = spyk()
        liveState.observe(owner, observer)
        liveState.observe(owner, observer1)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }


    @Test
    fun `observeForever multi observers`() {
        val observer1: Observer<String> = spyk()
        liveState.observeForever(observer)
        liveState.observeForever(observer1)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observeForever multi observers with post`() {
        val observer1: Observer<String> = spyk()
        liveState.observeForever(observer)
        liveState.observeForever(observer1)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start`() {
        owner.create()
        liveState.observe(owner, observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start with post`() {
        owner.create()
        liveState.observe(owner, observer)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after emit event`() {
        owner.create()
        liveState.setState { TEST_VALUE }
        liveState.observe(owner, observer)
        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after emit event with post`() {
        owner.create()
        liveState.postState { TEST_VALUE }
        liveState.observe(owner, observer)
        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start with multi observers`() {
        owner.create()
        val observer1: Observer<String> = spyk()
        liveState.observe(owner, observer)
        liveState.observe(owner, observer1)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }

        owner.start()
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start with multi observers with post`() {
        owner.create()
        val observer1: Observer<String> = spyk()
        liveState.observe(owner, observer)
        liveState.observe(owner, observer1)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }

        owner.start()
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after stop`() {
        owner.stop()
        liveState.observe(owner, observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after stop with post`() {
        owner.stop()
        liveState.observe(owner, observer)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start again`() {
        owner.stop()
        liveState.observe(owner, observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }

        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after start again with post`() {
        owner.stop()
        liveState.observe(owner, observer)
        liveState.postState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }

        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after one observation`() {
        owner.start()
        liveState.observe(owner, observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        owner.stop()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        owner.start()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after one observation multi owner`() {
        owner.start()
        liveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1: Observer<String> = spyk()
        owner1.start()

        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        liveState.observe(owner1, observer1)
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }

        liveState.setState { TEST_VALUE }
        verify(atLeast = 2) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after one observation with new owner`() {
        owner.start()
        liveState.observe(owner, observer)

        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        owner.destroy()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        owner = TestLifecycleOwner()
        observer = mockk()
        liveState.observe(owner, observer)
        owner.start()
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after one observation with new owner after start`() {
        owner.start()
        liveState.observe(owner, observer)

        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        owner.destroy()
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        owner = TestLifecycleOwner()
        observer = mockk()
        owner.start()
        liveState.observe(owner, observer)
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove`() {
        owner.start()
        liveState.observe(owner, observer)

        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        liveState.removeObserver(observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove before emit`() {
        owner.start()
        liveState.observe(owner, observer)
        liveState.removeObserver(observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove owner`() {
        owner.start()
        liveState.observe(owner, observer)

        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }

        liveState.removeObservers(owner)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove owner before emit`() {
        owner.start()
        liveState.observe(owner, observer)
        liveState.removeObservers(owner)

        liveState.setState { TEST_VALUE }
        verify(atLeast = 0) { observer.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove multi owner`() {
        owner.start()
        liveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1: Observer<String> = spyk()
        owner1.start()
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }

        liveState.observe(owner1, observer1)
        liveState.removeObserver(observer)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    @Test
    fun `observe after remove owner multi owner`() {
        owner.start()
        liveState.observe(owner, observer)
        val owner1 = TestLifecycleOwner()
        val observer1: Observer<String> = spyk()
        owner1.start()
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 0) { observer1.onChanged(TEST_VALUE) }

        liveState.observe(owner1, observer1)
        liveState.removeObservers(owner)
        liveState.setState { TEST_VALUE }
        verify(atLeast = 1) { observer.onChanged(TEST_VALUE) }
        verify(atLeast = 1) { observer1.onChanged(TEST_VALUE) }
    }

    companion object {
        private const val INITIAL_VALUE = "initialValue"
        private const val TEST_VALUE = "test-value"
    }
}
