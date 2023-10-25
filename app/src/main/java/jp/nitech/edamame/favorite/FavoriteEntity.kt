package jp.nitech.edamame.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val destination: String,
    val arrivalTime: LocalTime,
    val order: Int,
)