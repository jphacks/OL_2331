package jp.nitech.edamame.room.converter

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalTime? {
        return value?.let { LocalTime.ofNanoOfDay(value) }
    }

    @TypeConverter
    fun dateToTimestamp(time: LocalTime?): Long? {
        return time?.toNanoOfDay()
    }
}
