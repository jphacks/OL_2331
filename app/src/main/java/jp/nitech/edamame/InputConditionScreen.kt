package jp.nitech.edamame

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import jp.nitech.edamame.extension.formatCommaSplit
import jp.nitech.edamame.screenresult.rememberScreenResultConsumer
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@Composable
fun InputConditionScreen(navController: NavController) {
    val context = LocalContext.current

    var place by remember {
        mutableStateOf<Place?>(null)
    }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val dialogStateDate = rememberMaterialDialogState()
    var dialogStateTime = rememberMaterialDialogState()

    val selectedPlaceScreenResultConsumer = rememberScreenResultConsumer<Place?>(
        key = ScreenResultKeys.PLACE,
        provideEmitted = { key -> navController.currentBackStackEntry?.savedStateHandle?.remove(key) },
    )
    
    LaunchedEffect(Unit) {
        selectedPlaceScreenResultConsumer.consume()?.let {
            place = it
        }
    }

    Scaffold(
        topBar = {
            EdamameAppBar(
                title = "入力画面",
                right = {
                    Row {
                        Icon(
                            Icons.Default.Favorite,
                            "",
                            tint = Color(0xFFff0000),
                            modifier = Modifier
                                .height(70.dp)
                                .padding(end = 5.dp)
                                .clickable { navController.navigate(Screen.Favorites.route) }
                        )
                        Icon(
                            Icons.Default.Settings,
                            "",
                            modifier = Modifier
                                .height(70.dp)
                                .padding(end = 5.dp)
                                .clickable { navController.navigate(Screen.Settings.route) }
                        )
                    }
                },
                isBackButtonShown = false,
            )
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                //.background(
                //color = Color(0xFFffc1ff)
                //)
            ) {
                destination(
                    place = place,
                    onClick = {navController.navigate(Screen.SelectDestinationMap.route)},
                )
                Spacer(Modifier.size(20.dp))
                time(date.value,
                    time.value,
                    dialogStateDate,
                    dialogStateTime,
                    ondatePicked = { date.value = it.format(formatterDate) },
                    ontimePicked = { time.value = it.format(formatterTime) }
                )
                Spacer(Modifier.size(20.dp))
                todo(
                    onButtonClicked = {navController.navigate(Screen.Settings.route)}
                )
                Spacer(Modifier.size(40.dp))
                decision(time.value,place,onButtonClicked = {
                    val place = place ?: return@decision
                    val arguments = mutableMapOf<String, String>()
                    arguments["arrivalDate"] = date.value
                    arguments["arrivalTime"] = time.value
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
                })
            }
        }
    )
}

@Composable
private fun destination(
    place: Place?,
    onClick: () -> Unit,
) {
    val destinationText = if (place != null) {
        place.placeName ?: "${place.latLng.latitude},${place.latLng.longitude}"
    } else {
        ""
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "目的地", fontSize = 25.sp)
        OutlinedTextField(
            value = destinationText,
            onValueChange = { },
            enabled = false,
            placeholder = { Text(text = "目的地を選択してください") },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = MaterialTheme.colors.primary,
                backgroundColor = Color(0xFFf0f0f0),
            ),
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
fun time(
    date: String, time: String, dialogStateDate: MaterialDialogState,
    dialogStateTime: MaterialDialogState, ondatePicked: (LocalDate) -> Unit,
    ontimePicked: (LocalTime) -> Unit
) {
    var context = LocalContext.current


    MaterialDialog(
        dialogState = dialogStateDate,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker {
            ondatePicked(it)

            dialogStateTime.show()
            //Do stuff with java.time.LocalDate object which is passed in
        }
    }


    MaterialDialog(
        dialogState = dialogStateTime,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker {
            ontimePicked(it)
            //Do stuff with java.time.LocalDate object which is passed in
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "到着時間", fontSize = 25.sp)
        OutlinedTextField(
            value = "$date $time",
            onValueChange = {},
            enabled = false,
            placeholder = { Text(text = "到着時間を選択してください") },
            modifier = Modifier
                .clickable {
                    dialogStateDate.show()
                },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = MaterialTheme.colors.primary,
                backgroundColor = Color(0xFFf0f0f0)
            )
        )
    }
}

@Composable
fun decision(time: String,place: Place ?, onButtonClicked: () -> Unit) {
    if(time != "" && place != null) {
        Button(onClick = { onButtonClicked() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)

        ) {
            Text(text = "確定", fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White)
        }
    }else{Text(text = "")}
}

@Composable
fun todo(onButtonClicked: () -> Unit){
    Button(onClick = { onButtonClicked() }){
        Text(text = "To Do List", fontSize = 30.sp)
    }
}
