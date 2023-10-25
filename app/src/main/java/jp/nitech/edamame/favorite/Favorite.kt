package jp.nitech.edamame.favorite

import java.time.LocalTime

data class Favorite(
    val id: Long,
    val destination: String,
    val arrivalTime: LocalTime,
    val order: Int
) {

    fun toEntity() : FavoriteEntity {
        return FavoriteEntity(
            this.id,
            this.destination,
            this.arrivalTime,
            this.order,
        )
    }

    companion object {
        fun fromEntity(entity: FavoriteEntity) : Favorite {
            return Favorite(
                entity.id,
                entity.destination,
                entity.arrivalTime,
                entity.order,
            )
        }
    }
}
