package jp.nitech.edamame

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import jp.nitech.edamame.favorite.Favorite
import jp.nitech.edamame.favorite.FavoriteApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

class FavoriteScreenViewModel(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {

    private val _favorites = mutableStateListOf<Favorite>()
    val favorites = mutableStateOf(_favorites)

    private val favoriteApplication = FavoriteApplication()

    fun refreshFavorites() {
        val newFavorites = favoriteApplication.getFavorites().toMutableList()
        _favorites.clear()
        _favorites.addAll(newFavorites)
    }
}