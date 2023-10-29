package jp.nitech.edamame.steps

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.vector.ImageVector

fun findStepTypeIcon(stepType: StepType): ImageVector {
    return when (stepType) {
        StepType.PREPARE -> Icons.Default.Accessibility
        StepType.WALK -> Icons.AutoMirrored.Default.DirectionsWalk
        StepType.CAR -> Icons.Default.DirectionsCar
        StepType.BICYCLE -> Icons.Default.Settings
        StepType.DOMESTIC_FLIGHT -> Icons.Default.Flight
        StepType.SUPEREXPRESS_TRAIN -> Icons.Default.Train
        StepType.SLEEPER_ULTRAEXPRESS -> Icons.Default.Train
        StepType.ULTRAEXPRESS_TRAIN -> Icons.Default.Train
        StepType.EXPRESS_TRAIN -> Icons.Default.Train
        StepType.RAPID_TRAIN -> Icons.Default.Train
        StepType.SEMIEXPRESS_TRAIN -> Icons.Default.Train
        StepType.LOCAL_TRAIN -> Icons.Default.Train
        StepType.SHUTTLE_BUS -> Icons.Default.DirectionsBus
        StepType.LOCAL_BUS -> Icons.Default.DirectionsBus
        StepType.HIGHWAY_BUS -> Icons.Default.DirectionsBus
        StepType.UNKNOWN -> Icons.Default.QuestionMark
        StepType.GOAL -> Icons.Default.PinDrop
    }
}