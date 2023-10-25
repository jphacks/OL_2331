package jp.nitech.edamame.favorite

import android.content.Context
import jp.nitech.edamame.MainApplication
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime

class FavoriteApplication{

    private val favoriteDao = MainApplication.database.favoriteDao()

    fun getFavorites(): List<Favorite> {
        val favoritesDb = favoriteDao.getAll()
        return favoritesDb.map { Favorite.fromEntity(it) }
    }

    fun swapOrder(favoriteA: Favorite, favoriteB: Favorite) {
        val orderA = favoriteA.order
        val orderB = favoriteB.order
        val newFavoriteA = favoriteA.copy(order = orderB)
        val newFavoriteB = favoriteB.copy(order = orderA)
        val newFavoriteADb = newFavoriteA.toEntity()
        val newFavoriteBDb = newFavoriteB.toEntity()
        favoriteDao.insert(newFavoriteADb)
        favoriteDao.insert(newFavoriteBDb)
    }

    fun addFavorite(
        destination: String,
        arrivalTime: LocalTime,
    ): Favorite {
        val favoriteDb = FavoriteEntity(
            destination = destination,
            arrivalTime = arrivalTime,
            order = favoriteDao.lastOrder().value + 1
        )
        val id = favoriteDao.insert(favoriteDb)
        return Favorite.fromEntity(
            favoriteDb.copy(id = id)
        )
    }

    fun deleteFavorite(favorite: Favorite) {
        val favoriteDb = favorite.toEntity()
        favoriteDao.delete(favoriteDb)
    }

    fun deleteFavoriteById(id: Long) {
        favoriteDao.deleteById(id)
    }

}
