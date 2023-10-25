package jp.nitech.edamame

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsScreenViewModel(
    val context: Context,
    val coroutineScope: CoroutineScope,
){

    val minutesPreparing: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MINUTES_PREPARING_PREF_KEY] ?: 0
    }

    val walkingSpeed: Flow<WalkingSpeed> = context.dataStore.data.map { preferences ->
        val walkingSpeedStr = preferences[WALKING_SPEED_PREF_KEY] ?: WalkingSpeed.NORMAL.name
        return@map WalkingSpeed.valueOf(walkingSpeedStr)
    }

    suspend fun setMinutesPreparing(minutes: Int) {
        context.dataStore.edit { settings ->
            settings[MINUTES_PREPARING_PREF_KEY] = minutes
        }
    }

    suspend fun walkingSpeed(walkingSpeed: WalkingSpeed) {
        context.dataStore.edit { settings ->
            settings[WALKING_SPEED_PREF_KEY] = walkingSpeed.name
        }
    }

    private final val MINUTES_PREPARING_PREF_KEY = intPreferencesKey("MINUTES_PREPARING")
    private final val WALKING_SPEED_PREF_KEY = stringPreferencesKey("WALKING_SPEED")

}