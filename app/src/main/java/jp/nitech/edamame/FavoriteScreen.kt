package jp.nitech.edamame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jp.nitech.edamame.utils.rememberInMemory
import java.time.LocalTime


@Composable
fun FavScreen(){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val vm = rememberInMemory {
        FavoriteScreenViewModel(context, coroutineScope)
    }
    Scaffold(
        topBar = {
            EdamameAppBar(
                title = "お気に入り",
                right = {
                    Row {
                        Icon(Icons.Default.Settings, "")
                    }
                }
            )
        } ,
        content = { paddingValues ->
            Column (modifier = Modifier.padding(paddingValues)){
                for(i in 0 until vm.favorites.value.size) {
                    Text(text = vm.favorites.value[i].arrivalTime.format(formatterTime))
                    Text(text = vm.favorites.value[i].destinationPlaceName ?: "")
                }
            }
        }
    )
}
@Composable
fun favlist(SettingTime: LocalTime, SettingDest: String){
    var context = LocalContext.current
    var text = remember { mutableStateOf("") }

            Row {
                //お気に入りにした時間と場所の表示

            }
}