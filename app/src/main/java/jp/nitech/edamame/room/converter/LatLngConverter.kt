package jp.nitech.edamame.room.converter

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

class LatLngConverter {
    @TypeConverter
    fun fromString(value: String?): LatLng? {
        return value?.let {
            val split = it.split(",")
            LatLng(split[0].toDouble(), split[1].toDouble())
        }
    }

    @TypeConverter
    fun latlngToString(latlng: LatLng?): String? {
        return latlng?.let {
            "${it.latitude},${it.longitude}"
        }
    }
}
