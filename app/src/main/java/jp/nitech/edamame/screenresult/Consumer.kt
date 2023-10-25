package jp.nitech.edamame.screenresult

import androidx.compose.runtime.Composable

data class ScreenResultConsumer<R>(
    val consume: () -> R
)

@Composable
fun <R> rememberScreenResultConsumer(
    key: String,
    provideEmitted: (key: String) -> R,
): ScreenResultConsumer<R> {
    fun consume(): R = provideEmitted(key)

    return ScreenResultConsumer(
        consume = { consume() }
    )
}