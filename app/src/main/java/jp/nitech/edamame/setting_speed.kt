package jp.nitech.edamame

import android.content.Context
import android.view.Display.Mode
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun settingspeed() {
    val radioOptions = listOf("遅め" ,"普通","速め")
    val (selectedOption,onOptionSelected) = remember {
        mutableStateOf(radioOptions[1])
    }

    var speed by remember {
        mutableStateOf("")
    }
    val context= LocalContext.current
    LaunchedEffect(speed){
        GlobalScope.launch(Dispatchers.IO) {
            saveSpeed(context= context,"setting")
        }
    }


    Column {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected
                            speed = text

                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(selected = (text == selectedOption),
                onClick = {
                    onOptionSelected(text)

                })
                Text(
                    text=text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )

            }
        }
    }


}
val SPEED_KEY = stringPreferencesKey("example_text")
suspend fun saveSpeed(context: Context, speed: String ) {
    context.dataStore.edit { settings ->
        settings[SPEED_KEY] = speed
    }
}