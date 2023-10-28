package jp.nitech.edamame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import jp.nitech.edamame.screenresult.rememberScreenResultEmitter
import jp.nitech.edamame.utils.rememberInMemory

@Composable
fun SelectDestinationMapScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vm by rememberInMemory {
        mutableStateOf(SelectDestinationMapScreenViewModel(context, scope))
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            EdamameAppBar(title = "目的地を選択", onBackButtonClicked = {})
        },
        content = { paddingValues ->
            SelectDestinationMapContent(
                vm = vm,
                navController = navController,
                paddingValues = paddingValues,
            )
        }
    )
}

@Composable
private fun SelectDestinationMapContent(
    vm: SelectDestinationMapScreenViewModel,
    navController: NavController,
    paddingValues: PaddingValues,
) {
    val lastCurrentLatLng by vm.lastCurrentLatLng.collectAsState()
    val query = vm.query
    val placeCandidates = vm.placeCandidates
    val selectedPlace = vm.selectedPlace
    val isPlaceCandidatesShown = vm.isPlaceCandidatesShown

    val cameraPositionState = rememberCameraPositionState()
    val cameraZoomDefault = 14f
    val cameraZoomSearchResult = 16f

    val selectedPlaceScreenResultEmitter = rememberScreenResultEmitter<Place>(
        key = ScreenResultKeys.PLACE,
        onEmit = { key, value -> navController.previousBackStackEntry?.savedStateHandle?.set(key, value) },
        isEmittingEnabled = true,
    )

    LaunchedEffect(lastCurrentLatLng) {
        lastCurrentLatLng?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, cameraZoomDefault)
        }
    }

    fun onPlaceCandidateSelect(place: Place) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            place.latLng,
            cameraZoomSearchResult
        )
        vm.setSelectedPlace(place)
        vm.closePlaceCandidates()
    }

    fun onSearchButtonClick() {
        vm.searchPlaceCandidates()
    }

    fun onMapClick(position: LatLng) {
        vm.setSelectedPlace(Place(null, null, position))
        vm.closePlaceCandidates()
    }

    fun onConfirmButtonClick() {
        vm.selectedPlace?.let {selectedPlace ->
            selectedPlaceScreenResultEmitter.emitIfEmittingEnabled(selectedPlace)
            navController.popBackStack()
        }
    }

    Box(modifier = Modifier.padding(paddingValues)) {
        MapView(
            cameraPositionState = cameraPositionState,
            selectedPlace = selectedPlace,
            onMapClick = {onMapClick(it)}
        )
        SearchPlace(
            query = query,
            placeCandidates = placeCandidates,
            isPlaceCandidatesShown = isPlaceCandidatesShown,
            onQueryChange = vm::setQuery,
            onPlaceCandidateSelect = { onPlaceCandidateSelect(it) },
            onSearchButtonClick = { onSearchButtonClick() },
            modifier = Modifier
                .padding(16.dp)
                .zIndex(10f),
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            ConfirmButton(
                selectedPlace = selectedPlace,
                onClick = { onConfirmButtonClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MapView(
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    selectedPlace: Place?,
    onMapClick: (LatLng) -> Unit,
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick,
        uiSettings = MapUiSettings(myLocationButtonEnabled = true),
        properties = MapProperties(isMyLocationEnabled = true),
    ) {
        if (selectedPlace != null) {
            Marker(
                state = MarkerState(position = selectedPlace.latLng),
                title = selectedPlace?.let { it.placeName } ?: "",
            )
        }
    }
}

@Composable
private fun SearchPlace(
    query: String,
    placeCandidates: List<Place>,
    isPlaceCandidatesShown: Boolean,
    onQueryChange: (String) -> Unit,
    onPlaceCandidateSelect: (Place) -> Unit,
    onSearchButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
        ) {
            SearchTextFieldAndButton(
                query = query,
                onQueryChange = onQueryChange,
                onSearchButtonClick = onSearchButtonClick
            )
            PlaceCadidates(
                placeCandidates = placeCandidates,
                isPlaceCandidatesShown = isPlaceCandidatesShown,
                onPlaceCandidateSelect = onPlaceCandidateSelect,
            )
        }
    }
}

@Composable
private fun PlaceCandidate(
    place: Place,
    onClick: (Place) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .clickable { onClick(place) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = place.placeName ?: "",
            style = MaterialTheme.typography.body2,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = place.address ?: "",
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
private fun SearchTextFieldAndButton(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.height(IntrinsicSize.Max)) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            label = { Text(text = "目的地") },
            placeholder = { Text(text = "東京駅, 中部国際空港, ...") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onSearchButtonClick() }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun PlaceCadidates(
    placeCandidates: List<Place>,
    isPlaceCandidatesShown: Boolean,
    onPlaceCandidateSelect: (Place) -> Unit,
) {
    AnimatedVisibility(isPlaceCandidatesShown) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            placeCandidates.forEach { place ->
                PlaceCandidate(
                    place = place,
                    onClick = onPlaceCandidateSelect,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ConfirmButton(
    selectedPlace: Place?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(selectedPlace != null) {
        Button(onClick = onClick, modifier = modifier) {
            Text(text = "確定")
        }
    }
}