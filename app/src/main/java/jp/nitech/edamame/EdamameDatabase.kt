package jp.nitech.edamame

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.nitech.edamame.favorite.FavoriteDao
import jp.nitech.edamame.favorite.FavoriteEntity
import jp.nitech.edamame.room.converter.LocalTimeConverter

@Database(
    entities = [
        FavoriteEntity::class,

    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    LocalTimeConverter::class
)
abstract class EdamameDatabase : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao

}