package jp.nitech.edamame

import android.annotation.SuppressLint
import android.view.Display.Mode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.lightColors
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import androidx.compose.material.Surface as Surface1

//@Preview(showBackground = true, name = "Text Preview")


@Composable
fun settingscrean(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val navController = rememberNavController()
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
                    Main(navController = navController)
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
fun Main(navController: NavController) {
    Column {
        Button(onClick = {
            navController.navigate("prepare")
            Modifier.fillMaxWidth()
        }) {
            Text("準備時間")
        }
        Button(onClick = {
            navController.navigate("speed")
            Modifier.fillMaxWidth()
        }) {
            Text("歩く速さ")
        }


    }
}
//SuppressLint("SuspiciousIndentation")
@Composable
fun PrepareTime(
    modifier: Modifier,
    navController: NavHostController
    ) {

}

@Composable
fun Speed(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Main(navController = navController)
        }
        composable("speed") {
            settingspeed()
        }
    }
}




/*
    @Composable
    fun Speed(name: String) {
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
                    text = name,
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

*/