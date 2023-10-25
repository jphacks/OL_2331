package jp.nitech.edamame

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import jp.nitech.edamame.favorite.Favorite
import jp.nitech.edamame.favorite.FavoriteApplication
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime

class FavoriteScreenViewModel(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {

    private val _favorites = mutableStateListOf<Favorite>()
    val favorites = mutableStateOf(_favorites)

    private val favoriteApplication = FavoriteApplication()

    fun addFavorite(
        destination: String,
        arrivalTime: LocalTime,
    ) {
        val newFavorite = favoriteApplication.addFavorite(destination, arrivalTime)
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