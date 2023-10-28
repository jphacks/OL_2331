package jp.nitech.edamame.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val destinationPlaceName: String?,
    val destination: LatLng,
    val arrivalTime: LocalTime,
    val order: Int,
)