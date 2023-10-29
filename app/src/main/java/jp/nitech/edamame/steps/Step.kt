package jp.nitech.edamame.steps

import java.time.LocalTime

class Step(
    val startTime: LocalTime,
    val title: String,
    val detailMessage: String,
    val type: StepType
) {

}

enum class StepType(val navitimeAPIString: String) {
    PREPARE("prepare"),
    WALK("walk"),
    CAR("car"),
    BICYCLE("bicycle"),
    DOMESTIC_FLIGHT("domestic_flight"),
    SUPEREXPRESS_TRAIN("superexpress_train"),
    SLEEPER_ULTRAEXPRESS("sleeper_ultraexpress"),
    ULTRAEXPRESS_TRAIN("ultraexpress_train"),
    EXPRESS_TRAIN("express_train"),
    RAPID_TRAIN("rapid_train"),
    SEMIEXPRESS_TRAIN("semiexpress_train"),
    LOCAL_TRAIN("local_train"),
    SHUTTLE_BUS("shuttle_bus"),
    LOCAL_BUS("local_bus"),
    HIGHWAY_BUS("highway_bus"),
    UNKNOWN("unknown"),
    GOAL("__goal__");

    companion object {
        fun fromNaviTimeAPIString(navitimeAPIString: String): StepType? =
            StepType.values().find { it.navitimeAPIString == navitimeAPIString }

    }

}