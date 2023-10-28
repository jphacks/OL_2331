package jp.nitech.edamame

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jp.nitech.edamame.extension.toLatLng
import jp.nitech.edamame.ui.theme.TestTheme


@Composable
private fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Loading.routeWithArgs) {
        composable(Screen.InputCondition.routeWithArgs) {
            InputConditionScreen(navController = navController)
        }
        composable(
            Screen.Result.routeWithArgs,
            arguments = listOf(
                navArgument("favoriteId") { nullable = true },
                navArgument("arrivalDate") { nullable = false },
                navArgument("arrivalTime") { nullable = false },
                navArgument("destinationLatLng") { nullable = false },
                navArgument("destinationPlaceName") { nullable = true },
                navArgument("destinationAddress") { nullable = true },
            )
        ) { navBackStackEntry ->
            val arguments = navBackStackEntry.arguments
            if (arguments == null) {
                Text("arguments is null")
                return@composable
            }

            ResultScreen(
                favoriteId = arguments.getLong("favoriteId"),
                arrivalDate = arguments.getString("arrivalDate")!!,
                arrivalTime = arguments.getString("arrivalTime")!!,
                destinationLatLng = arguments.getString("destinationLatLng")!!.toLatLng(),
                destinationPlaceName = arguments.getString("destinationPlaceName"),
                destinationAddress = arguments.getString("destinationAddress"),
            )
        }
        composable(Screen.SelectDestinationMap.routeWithArgs) {
            SelectDestinationMapScreen(navController = navController)
        }
        composable(Screen.Settings.routeWithArgs) {
            settingscrean(navController = navController)
        }
        composable(Screen.Favorites.routeWithArgs) {
            FavScreen()
        }
        composable(Screen.Loading.routeWithArgs) {
            LoadingScreen(navController = navController)
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

    object Result : Screen(
        route = "result",
        routeWithArgs = "result?favoriteId={favoriteId}&arrivalDate={arrivalDate}&arrivalTime={arrivalTime}&destinationLatLng={destinationLatLng}&destinationPlaceName={destinationPlaceName}&destinationAddress={destinationAddress}"
    )

    object SelectDestinationMap : Screen(
        route = "map",
    )

    object Settings : Screen(
        route = "settings",
    )

    object Favorites : Screen(
        route = "favorites"
    )

    object Loading : Screen(
        route = "loading"
    )

    object Debug : Screen(
        route = "debug",
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.prepareData: DataStore<Preferences> by preferencesDataStore(name = "preparetime")