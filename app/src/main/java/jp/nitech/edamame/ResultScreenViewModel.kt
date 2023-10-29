package jp.nitech.edamame

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.google.android.gms.maps.model.LatLng
import jp.nitech.edamame.favorite.FavoriteApplication
import jp.nitech.edamame.steps.Step
import jp.nitech.edamame.steps.StepsApplication
import jp.nitech.edamame.steps.WalkingSpeed
import jp.nitech.edamame.utils.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ResultScreenViewModel(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val favoriteApplication = FavoriteApplication()

    var isFavorited by mutableStateOf(false)
        private set

    var favoriteId by mutableStateOf<Long?>(null)
    var destination by mutableStateOf<Place?>(null)
    var arrivalDate by mutableStateOf("")
    var arrivalTime by mutableStateOf("")

    private var _steps = MutableStateFlow<List<Step>?>(null)
    val steps = _steps.asStateFlow()

    private val _lastCurrentLatLng = MutableStateFlow<LatLng?>(null)
    val lastCurrentLatLng = _lastCurrentLatLng.asStateFlow()

    val minutesPreparingFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MINUTES_PREPARING_PREF_KEY] ?: 0
    }
    var minutesPreparing = MutableStateFlow<Int?>(null)

    val walkingSpeedFlow: Flow<WalkingSpeed> = context.dataStore.data.map { preferences ->
        val walkingSpeedStr = preferences[WALKING_SPEED_PREF_KEY] ?: WalkingSpeed.NORMAL.name
        return@map WalkingSpeed.valueOf(walkingSpeedStr)
    }
    var walkingSpeed = MutableStateFlow<WalkingSpeed?>(null)

    init {
        _lastCurrentLatLng.tryEmit(LocationUtil.getCurrentLatLng(context))
        coroutineScope.launch{
            walkingSpeedFlow.collect { v ->
                walkingSpeed.emit(v)
            }
        }
        coroutineScope.launch {
            minutesPreparingFlow.collect { v ->
                minutesPreparing.emit(v)
            }
        }
    }

    fun exploreSteps() {
        // リクエスト用の変数にコンバート
        val arrivalDate = LocalDate.parse(this.arrivalDate, formatterDate)
        val arrivalTime = LocalTime.parse(this.arrivalTime, formatterTime)
        val arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime)
        val start = lastCurrentLatLng.value ?: return
        val goal = destination?.latLng ?: return
        val minutesPreparing = this.minutesPreparing.value ?: return
        val walkingSpeed = this.walkingSpeed.value ?: return

        // Request
        val appInfo = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val apiKey = appInfo.metaData.getString("com.rapidapi.navitime.API_KEY") ?: return
        val stepsApplication = StepsApplication(apiKey)
        // TODO: Load preparation info.
        val exploreStepsRequest = StepsApplication.ExploreStepsRequest(
            arrivalDateTime = arrivalDateTime,
            start = start,
            goal = goal,
            preparingTodos = listOf(), // TODO: Load preparation todos.
            preparingMinutes = minutesPreparing,
        )
        // TODO: Load walking speed.
        val exploreStepSettings = StepsApplication.ExploreStepsSettings(
            walkingSpeed = walkingSpeed,
        )
        val exploreStepsResult = stepsApplication.exploreSteps(
            exploreStepsRequest,
            exploreStepSettings,
        )

        // NOTE: Not implemented when error
        exploreStepsResult.onSuccess {steps ->
            this._steps.tryEmit(steps)
        }.onFailure {
            Log.w("ResultScreenViewModel", "Failed to get steps: ${it.printStackTrace()}")
        }
    }

    /**
     * お気に入りから取得してきたデータを設定します
     */
    fun putFavorite(
        favoriteId: Long,
        place: Place,
        arrivalDate: LocalDate,
        arrivalTime: LocalTime,
    ) {
        this.isFavorited = true
        this.favoriteId = favoriteId
        this.destination = place
        this.arrivalDate = arrivalDate.format(formatterDate)
        this.arrivalTime = arrivalTime.format(formatterTime)
    }

    fun like() {
        coroutineScope.launch(Dispatchers.IO) {
            val arrivalTime = arrivalTime ?: return@launch
            val destination = destination ?: return@launch

            isFavorited = true
            val favorite = favoriteApplication.addFavorite(
                destination.placeName,
                destination.latLng,
                LocalTime.parse(arrivalTime, formatterTime)
            )
            favoriteId = favorite.id
        }
    }

    fun unlike() {
        coroutineScope.launch(Dispatchers.IO) {
            val _favoriteId = favoriteId ?: return@launch

            isFavorited = false
            favoriteId = null
            favoriteApplication.deleteFavoriteById(_favoriteId)
        }
    }

    private final val MINUTES_PREPARING_PREF_KEY = intPreferencesKey("MINUTES_PREPARING")
    private final val WALKING_SPEED_PREF_KEY = stringPreferencesKey("WALKING_SPEED")

}
