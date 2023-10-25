package jp.nitech.edamame.favorite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("select * from favorites")
    fun getAll(): MutableList<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntity): Long

    @Delete
    fun delete(favorite: FavoriteEntity)

    @Query("delete from favorites where id = :id")
    fun deleteById(id: Long)

    @Query("select MAX(favorites.`order`) as value from favorites")
    fun lastOrder(): LastOrderPojo

    data class LastOrderPojo(
        val value: Int
    )

}