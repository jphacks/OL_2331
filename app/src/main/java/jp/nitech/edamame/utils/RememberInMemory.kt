package jp.nitech.edamame.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Source code from https://stackoverflow.com/questions/76029193/remembersaveable-but-in-memory-instead-of-on-disk-like-viewmodel
 */

/**
 * A generic [ViewModel] class that will keep any state assigned to it in memory.
 */
class StateMapViewModel : ViewModel() {
    val states: MutableMap<Int, ArrayDeque<Any>> = mutableMapOf()
}

/**
 * Remember the value produced by [init].
 *
 * It behaves similarly to [remember], but the stored value will survive configuration changes
 * (for example when the screen is rotated in the Android application).
 * The value will not survive process recreation.
 *
 * @param inputs A set of inputs such that, when any of them have changed, will cause the state to
 * reset and [init] to be rerun
 * @param init A factory function to create the initial value of this state
 */
@Composable
inline fun <reified T : Any> rememberInMemory(
    vararg inputs: Any?,
    crossinline init: @DisallowComposableCalls () -> T,
): T {
    val vm: StateMapViewModel = viewModel()

    val key = currentCompositeKeyHash

    val value = remember(*inputs) {
        val states = vm.states[key] ?: ArrayDeque<Any>().also { vm.states[key] = it }
        states.removeFirstOrNull() as T? ?: init()
    }

    val valueState = rememberUpdatedState(value)

    DisposableEffect(key) {
        onDispose {
            vm.states[key]?.addFirst(valueState.value)
        }
    }

    return valueState.value
}

@Composable
inline fun <reified T : Any> rememberInMemory(
    crossinline init: @DisallowComposableCalls () -> T,
): T {
    return rememberInMemory(null, init = init)
}