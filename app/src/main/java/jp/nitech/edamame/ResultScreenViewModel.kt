package jp.nitech.edamame

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import jp.nitech.edamame.favorite.FavoriteApplication
import jp.nitech.edamame.steps.Step
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

class ResultScreenViewModel(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val favoriteApplication = FavoriteApplication()

    var isFavorited by mutableStateOf(false)
        private set

    var favoriteId by mutableStateOf<Long?>(null)
    var destination by mutableStateOf("meiko")
    var arrivalDate by mutableStateOf("")
    var arrivalTime by mutableStateOf("11:50")

    private var _steps = MutableStateFlow<List<Step>?>(null)
    val steps = _steps.asStateFlow()

    suspend fun exploreSteps() {
        // TODO: Implement
    }

    /**
     * お気に入りから取得してきたデータを設定します
     */
    fun putFavorite(
        favoriteId: Long,
        destination: String,
        arrivalTime: LocalTime,
    ) {
        this.isFavorited = true
        this.favoriteId = favoriteId
        this.destination = destination
        this.arrivalDate = arrivalDate.format(formatterDate)
        this.arrivalTime = arrivalTime.format(formatterTime)
    }

    fun like() {
        coroutineScope.launch(Dispatchers.IO) {
            val arrivalTime = arrivalTime ?: return@launch

            isFavorited = true
            val favorite = favoriteApplication.addFavorite(
                destination,
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
}
