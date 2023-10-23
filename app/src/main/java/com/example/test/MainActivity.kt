package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.TestTheme

@Composable
private fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Debug.routeWithArgs) {
        composable(Screen.InputCondition.routeWithArgs) {
            samplescreen2()
        }
        composable(Screen.WhenShouldYouWakeUpResult.routeWithArgs) {
            Text(text = "TODO: リザルト画面")
        }
        composable(Screen.SelectDestinationMap.routeWithArgs) {
            Text(text = "TODO: 目的地選択画面")
        }
        composable(Screen.Settings.routeWithArgs) {
            samplescreen()
        }
        composable(Screen.Debug.routeWithArgs) {
            DebugScreen(navController = navController)
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()

    TestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            AppNavigator(navController)
        }
    }
}

sealed class Screen(
    val route: String,
    val routeWithArgs: String = route,
) {
    object InputCondition : Screen(
        route = "inputCondition",
    )

    object WhenShouldYouWakeUpResult : Screen(
        route = "whenShouldYouWaleUpResult",
        routeWithArgs = "whenShouldYouWaleUpResult?arrivalTime={arrivalTime}&destination={destination}",
    )

    object SelectDestinationMap : Screen(
        route = "map",
    )

    object Settings : Screen(
        route = "settings",
    )

    object Debug : Screen(
        route = "debug",
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
