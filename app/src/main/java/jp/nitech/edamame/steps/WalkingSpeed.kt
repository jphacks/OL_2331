package jp.nitech.edamame.steps

/**
 * 歩く速度を表します。
 */
enum class WalkingSpeed(val km_per_hour: Double) {
    SLOW(3.0),
    NORMAL(4.5),
    FAST(6.0),
}