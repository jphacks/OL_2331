package jp.nitech.edamame

import android.content.Context
import android.content.pm.PackageManager
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import jp.nitech.edamame.extension.convertLib
import jp.nitech.edamame.utils.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class SelectDestinationMapScreenViewModel(
    val context: Context,
    val coroutineScope: CoroutineScope,
) {

    private val _lastCurrentLatLng = MutableStateFlow<LatLng?>(null)
    val lastCurrentLatLng = _lastCurrentLatLng.asStateFlow()

    private var _query = mutableStateOf("")
    val query by _query

    private var _placeCandidates = mutableStateListOf<Place>()
    val placeCandidates = _placeCandidates

    private var _selectedPlace = mutableStateOf<Place?>(null)
    val selectedPlace by _selectedPlace

    private var _isPlaceCandidatesShown = mutableStateOf(false)
    val isPlaceCandidatesShown by _isPlaceCandidatesShown

    init {
        _lastCurrentLatLng.tryEmit(LocationUtil.getCurrentLatLng(context))
    }

    fun searchPlaceCandidates() {
        if (query.isBlank()) return

        coroutineScope.launch(Dispatchers.IO) {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")
            val geoApiContext = GeoApiContext.Builder().apiKey(apiKey).build()
            val searchResponse = PlacesApi.textSearchQuery(
                geoApiContext,
                query,
            )
                .language("ja")
                .await()
            _placeCandidates.clear()
            searchResponse.results.forEach { placeCandidate ->
                if (
                    placeCandidate.name == null ||
                    placeCandidate.formattedAddress == null ||
                    placeCandidate.geometry == null
                ) {
                    return@forEach
                }
                _placeCandidates.add(Place(
                    placeCandidate.name ?: placeCandidate.formattedAddress,
                    placeCandidate.formattedAddress,
                    placeCandidate.geometry.location.convertLib(),
                ))
            }
            openPlaceCandidates()
        }
    }

    fun setSelectedPlace(place: Place) {
        _selectedPlace.value = place
    }

    fun openPlaceCandidates() {
        _isPlaceCandidatesShown.value = true
    }

    fun closePlaceCandidates() {
        _isPlaceCandidatesShown.value = false
    }

    fun setQuery(query: String) {
        _query.value = query
    }

}

@Parcelize
data class Place(
    val placeName: String?,
    val address: String?,
    val latLng: LatLng,
) : Parcelable
