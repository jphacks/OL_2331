package jp.nitech.edamame

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.nitech.edamame.utils.rememberInMemory


//@Preview(showBackground = true, name = "Text Preview")


@Composable
fun settingscrean(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val navController = rememberNavController()
    val context= LocalContext.current
    val coroutineScope= rememberCoroutineScope()
    val vm= rememberInMemory {
        SettingsScreenViewModel(context, coroutineScope)
    }
    Scaffold(
        topBar = {
            EdamameAppBar(
                title = "設定画面",
                right = {
                    Row {
                        Icon(
                            Icons.Default.Favorite,
                            "",
                            tint = Color(0xFFff0000),
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Favorites.route) }
                        )
                        //Icon(Icons.Default.Settings, "")
                    }
                }
            )
        },
        content = { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = "main",
                modifier=Modifier.padding(paddingValues)) {
                composable("main") {
                    Main(navController = navController,vm)
                }
                composable("prepare") {
                    settingprepare()
                }
                composable("speed") {
                    settingspeed()
                }
            }
            //Setting(Modifier.padding(paddingValues),navController)
        }
    )


}


@Composable
fun Main(navController: NavController,vm:SettingsScreenViewModel) {
    val minutesPreparing by vm.minutesPreparing.collectAsState(initial = 0)
    val walkingSpeed by vm.walkingSpeed.collectAsState(initial = "普通")
    Column {
        Modifier.padding(10.dp)
        Button(onClick = {
            navController.navigate("prepare")

        },
            modifier=Modifier.padding(10.dp).width(400.dp)
        ) {
            vm.minutesPreparing

            Row(
                Modifier.fillMaxWidth(),
                //horizontalArrangement = Arrangement.End
            ) {
                Text("準備時間", textAlign = TextAlign.Start)
                Spacer(Modifier.weight(1f))
                Text(minutesPreparing.toString(), textAlign = TextAlign.End)
                Text(text = "分")
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    "",
                    tint = Color(0xFFFFFFFF),

                )
            }
        }

        Button(onClick = {
            navController.navigate("speed")

        },modifier=Modifier.padding(10.dp).width(400.dp)
        ) {
            vm.walkingSpeed
            Row(
                Modifier.fillMaxWidth(),
            ) {
                Text("歩く速さ")
                Spacer(Modifier.weight(1f))
                Text(walkingSpeed.toString())
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    "",
                    tint = Color(0xFFFFFFFF),

                    )


            }
        }


    }
}
//SuppressLint("SuspiciousIndentation")
