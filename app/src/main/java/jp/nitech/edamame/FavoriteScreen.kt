package jp.nitech.edamame

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import jp.nitech.edamame.extension.formatCommaSplit
import jp.nitech.edamame.utils.rememberInMemory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun FavScreen(date: LocalDate,navController: NavController){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val vm = rememberInMemory {
        FavoriteScreenViewModel(context, coroutineScope)
    }
    LaunchedEffect(Unit){
        coroutineScope.launch(Dispatchers.IO) {
            vm.refreshFavorites()
        }
    }
    Scaffold(
        topBar = {
            EdamameAppBar(
                title = "お気に入り",
                right = {
                    Row() {
                        Icon(Icons.Default.Settings, "",
                            modifier = Modifier
                                .height(70.dp)
                                .padding(end = 5.dp)
                                .clickable { navController.navigate(Screen.Settings.route) })
                    }
                }
            )
        } ,
        content = { paddingValues ->
            Column (modifier = Modifier.padding(paddingValues)){
                for(i in 0 until vm.favorites.value.size) {
                    favlist(
                        vm.favorites.value[i].arrivalTime,
                        vm.favorites.value[i].destinationPlaceName ?: "",
                        onButtonClicked = {
                            val place = Place(vm.favorites.value[i].destinationPlaceName,
                                null,
                                vm.favorites.value[i].destination
                                )
                            val arguments = mutableMapOf<String, String>()
                            arguments["arrivalDate"] = formatterDate.format(date)//次の日にしたい
                            arguments["arrivalTime"] = vm.favorites.value[i].arrivalTime.toString()
                            arguments["destinationLatLng"] = place.latLng.formatCommaSplit()
                            if (place.placeName != null) {
                                arguments["destinationPlaceName"] = place.placeName
                            }
                            if (place.address != null) {
                                arguments["destinationAddress"] = place.address
                            }
                            val argumentsQuery = arguments
                                .map { "${it.key}=${it.value}" }
                                .joinToString("&")
                            navController.navigate("${Screen.Result.route}?${argumentsQuery}")
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun favlist(SettingTime: LocalTime, SettingDest: String,onButtonClicked: () -> Unit){
    var context = LocalContext.current
    var text = remember { mutableStateOf("") }

    Column(
        modifier = Modifier,//.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

            Button(onClick = { onButtonClicked() },
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(20.dp)
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                        /*.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        )
                        .verticalScroll(rememberScrollState()),*/
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                //お気に入りにした時間と場所の表示
                Text(text = SettingTime.format(formatterTime), fontSize = 30.sp)
                Spacer(Modifier.size(30.dp))
                Text(text = SettingDest, fontSize = 25.sp,
                    modifier = Modifier
                        .padding(top = 8.dp))
            }

        }
        Divider(
            modifier = Modifier.height(2.dp),
            color = Color(0xFF000000)
        )
    }
}