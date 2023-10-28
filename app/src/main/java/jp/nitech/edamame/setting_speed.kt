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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jp.nitech.edamame.steps.WalkingSpeed
import jp.nitech.edamame.utils.rememberInMemory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun settingspeed() {
    val context= LocalContext.current
    val coroutineScope= rememberCoroutineScope()
    val vm = rememberInMemory {
        SettingsScreenViewModel(context, coroutineScope)
    }
    //val radioOptions = listOf("遅め" ,"普通","速め")
    //val (selectedOption,onOptionSelected) = remember {
    //    mutableStateOf(radioOptions[1])
    val walkingSpeed by vm.walkingSpeed.collectAsState(initial = WalkingSpeed.NORMAL)

    Column {
        Modifier.padding(10.dp)
        Options(name = "SLOW", select = (walkingSpeed==WalkingSpeed.SLOW) , onClick = {
            GlobalScope.launch(Dispatchers.IO){
                vm.walkingSpeed(WalkingSpeed.SLOW)
            }
        } )
        Options(name = "NORMAL", select = (walkingSpeed==WalkingSpeed.NORMAL) , onClick = {
            GlobalScope.launch(Dispatchers.IO){
                vm.walkingSpeed(WalkingSpeed.NORMAL)
            }
        } )
        Options(name = "FAST", select = (walkingSpeed==WalkingSpeed.FAST) , onClick = {
            GlobalScope.launch(Dispatchers.IO){
                vm.walkingSpeed(WalkingSpeed.FAST)
            }
        } )
    }


}


@Composable
fun Options(name:String,select:Boolean,onClick:()->Unit){
    Row(

        Modifier
            .fillMaxWidth()
            .selectable(
                selected = select,
                onClick = onClick
            )
            .padding(horizontal = 20.dp)
    ) {
        RadioButton(
            selected = select,
            onClick = onClick
        )


        Text(
            text = name,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    }

