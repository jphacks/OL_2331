package jp.nitech.edamame

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import jp.nitech.edamame.favorite.Favorite
import jp.nitech.edamame.favorite.FavoriteApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DebugScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val vm = remember {
        DebugScreenViewModel(context, coroutineScope)
    }

    val favorites by vm.favorites

    var destination by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            vm.refreshFavorites()
        }
    }

    Scaffold(
        topBar = { EdamameAppBar(
            title = "えだまめぷりぷり"
        ) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { navController.navigate(Screen.InputCondition.route) }) {
                    Text(text = "検索条件入力画面に遷移")
                }
                Button(onClick = { navController.navigate(Screen.Result.route) }) {
                    Text(text = "検索結果画面に遷移")
                }
                Button(onClick = { navController.navigate(Screen.SelectDestinationMap.route) }) {
                    Text(text = "目的地選択画面に遷移")
                }
                Button(onClick = { navController.navigate(Screen.Favorites.route) }) {
                    Text(text = "お気に入り一覧画面に遷移")
                }
                Button(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Text(text = "設定画面に遷移")
                }
                Spacer(modifier = Modifier.height(32.dp))
                Column {
                    favorites.forEach {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${it.id} / ${it.arrivalTime.format(DateTimeFormatter.ISO_TIME)} / ${it.destinationPlaceName}")
                            Button(onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    vm.deleteFavorite(it)
                                }
                            }) {
                                Text(text = "✗")
                            }
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Destination:")
                    TextField(
                        value = destination,
                        onValueChange = { destination = it },
                        singleLine = true,
                    )
                }
                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        vm.addFavorite(destination, LocalTime.now())
                        destination = ""
                    }
                }) {
                    Text(text = "Insert")
                }

            }
        }
    )
}

private class DebugScreenViewModel(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {

    private val _favorites = mutableStateListOf<Favorite>()
    val favorites = mutableStateOf(_favorites)

    private val favoriteApplication = FavoriteApplication()

    fun addFavorite(
        destinationPlaceName: String,
        arrivalTime: LocalTime,
    ) {
        val newFavorite = favoriteApplication.addFavorite(
            destinationPlaceName,
            LatLng(0.0, 0.0),
            arrivalTime
        )
        _favorites.add(newFavorite)
    }

    fun deleteFavorite(
        favorite: Favorite,
    ) {
        favoriteApplication.deleteFavorite(favorite)
        _favorites.remove(favorite)
    }

    fun refreshFavorites() {
        val newFavorites = favoriteApplication.getFavorites().toMutableList()
        _favorites.clear()
        _favorites.addAll(newFavorites)
    }
}