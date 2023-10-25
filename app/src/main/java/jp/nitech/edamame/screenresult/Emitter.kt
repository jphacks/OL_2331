package jp.nitech.edamame.screenresult

import androidx.compose.runtime.Composable

data class ScreenResultEmitter<R>(
    val isEmittingEnabled: Boolean,
    val emitIfEmittingEnabled: (R) -> Unit,
)

@Composable
fun <R> rememberScreenResultEmitter(
    key: String,
    onEmit: (key: String, value: R) -> Unit,
    isEmittingEnabled: Boolean,
): ScreenResultEmitter<R> {
    fun emitIfEmittingEnabled(value: R) {
        if (isEmittingEnabled) {
            onEmit(key, value)
        }
    }

    return ScreenResultEmitter(
        isEmittingEnabled = isEmittingEnabled,
        emitIfEmittingEnabled = { key -> emitIfEmittingEnabled(key) },
    )
}