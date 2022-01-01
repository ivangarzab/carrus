package com.ivangarzab.carbud.delegates

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Ivan Garza on 9/24/20.
 *
 * Delegate class to bind the [Fragment] with it's corresponding [ViewBinding] object.
 * We make sure to only hold a reference to the binding object from [Fragment.onCreateView]
 * to [Fragment.onDestroyView] by registering an observer to the [Fragment.getLifecycle].
 * Finally, we use the [FragmentViewBinder] to do all the reflection under the hood.
 *
 * @param fragmentBinder to be associated with [Fragment]
 * @param onClear High-order function to add some specialized teardown for any given [Fragment]
 */
class FragmentViewBinding<T : ViewBinding>(
    private val fragmentBinder: (Fragment) -> T,
    private val onClear: (() -> Unit)?
) : ReadOnlyProperty<Fragment, T> {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var viewBinding: T? = null
    private var fragmentRef: Fragment? = null
    private val lifecycleObserver = BindingLifecycleObserver()

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewBinding?.let { vb ->
            // if we've already generated this ViewBinding, return
            check(this.fragmentRef != null && thisRef === this.fragmentRef) {
                "Instance of ViewBindingProperty can't be shared between classes"
            }
            return vb
        }
        // Otherwise, we need to create our ViewBinding and register
        // a lifecycle observer on it.
        this.fragmentRef = thisRef
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return fragmentBinder(thisRef).also {
            viewBinding = it
        }
    }

    /**
     * Clears out held reference to the [Fragment] and unregisters [BindingLifecycleObserver].
     */
    @MainThread
    fun clear() {
        // Execute this function first in case we need some specialized teardown
        this.onClear?.invoke()
        val localRef = this.fragmentRef ?: return
        this.fragmentRef = null
        localRef.viewLifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        mainHandler.post {
            viewBinding = null
        }
    }

    /**
     * Simple lifecycle observer meant to clear itself [onDestroy] to avoid
     * leaking the ViewBinding object.
     */
    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {
        @MainThread
        override fun onDestroy(owner: LifecycleOwner) = clear()
    }
}

/**
 * Binder class to be used along with [FragmentViewBinding].
 * This uses reflection to cache the [bind] method from the [FragmentViewBinding]
 * and only use it when needed.
 *
 * @param viewBindingClass to be used for reflection.
 */
class FragmentViewBinder<T : ViewBinding>(
    private val viewBindingClass: Class<T>
) {
    /**
     * Cache [viewBindingClass]' [bind] method for later use.
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("bind", View::class.java)
    }

    /**
     * Bind [Fragment]'s View to [ViewBinding] and return instance.
     */
    @Suppress("UNCHECKED_CAST")
    fun bind(fragment: Fragment): T =
        bindViewMethod(null, fragment.requireView()) as T
}

/**
 * Fragment extension for creating a new [ViewBinding] associated with the given [Fragment]
 * while using reflection under the hood.
 */
@Suppress("unused")
inline fun <reified T : ViewBinding> Fragment.viewBinding(noinline onClear: (() -> Unit)? = null):
        ReadOnlyProperty<Fragment, T> =
    FragmentViewBinding(FragmentViewBinder(T::class.java)::bind, onClear)