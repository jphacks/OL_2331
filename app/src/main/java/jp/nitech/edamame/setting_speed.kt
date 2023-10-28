package jp.nitech.edamame

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun settingspeed() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var expanded1 = remember { mutableStateOf(false) }
    var expanded2 = remember { mutableStateOf(false) }
    var expanded3 = remember { mutableStateOf(false) }
    val extraPdding1 = if (expanded1.value) 48.dp else 0.dp
    val extraPdding2 = if (expanded2.value) 48.dp else 0.dp
    val extraPdding3 = if (expanded3.value) 48.dp else 0.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Box {
            Text(
                text = "歩く速度",
                //modifier = Modifier.align(Alignment.TopStart)

            )
        }
        Button(
            onClick = { expanded1.value = !expanded1.value },
            interactionSource = interactionSource
        ) {
            Text(if (expanded1.value) "Pressed" else "遅め")
        }
        Button(
            onClick = { expanded2.value = !expanded2.value },
            interactionSource = interactionSource
        ) {
            Text(if (expanded2.value) "Pressed" else "ふつう")
        }
        Button(
            onClick = { expanded3.value = !expanded3.value },
            interactionSource = interactionSource
        ) {
            Text(if (expanded3.value) "Pressed" else "早め")
        }

    }

}