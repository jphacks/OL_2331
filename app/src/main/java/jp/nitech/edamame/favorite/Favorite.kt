package jp.nitech.edamame.favorite

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

data class Favorite(
    val id: Long,
    val destinationPlaceName: String?,
    val destination: LatLng,
    val arrivalTime: LocalTime,
    val order: Int
) {

    fun toEntity() : FavoriteEntity {
        return FavoriteEntity(
            this.id,
            this.destinationPlaceName,
            this.destination,
            this.arrivalTime,
            this.order,
        )
    }

    companion object {
        fun fromEntity(entity: FavoriteEntity) : Favorite {
            return Favorite(
                entity.id,
                entity.destinationPlaceName,
                entity.destination,
                entity.arrivalTime,
                entity.order,
            )
        }
    }
}
