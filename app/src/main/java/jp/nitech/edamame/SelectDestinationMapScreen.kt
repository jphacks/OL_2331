package jp.nitech.edamame

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TransitMode
import com.google.maps.model.TransitRoutingPreference
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SelectDestinationMapScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var routeSummaries by remember {
        mutableStateOf<List<DirectionsRoute>?>(null)
    }

    val singapore = LatLng(1.35, 103.87)
    val gifu = LatLng(35.447227, 136.756165)
    val tokyo = LatLng(35.652832, 139.839478)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//        onMapClick = { Toast.makeText(context, it.latitude.toString(), Toast.LENGTH_LONG).show() },
//    ) {
//
//    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")
            val geoApiContext = GeoApiContext.Builder().apiKey(apiKey).build()
            val directions = DirectionsApi
                .getDirections(
                    geoApiContext,
                    "ChIJ8dymrL5wA2AR1UCWVPCShvs",
                    "ChIJC3Cf2PuLGGAROO00ukl8JwA",
                )
                .originPlaceId("ChIJ8dymrL5wA2AR1UCWVPCShvs")
                .destinationPlaceId("ChIJC3Cf2PuLGGAROO00ukl8JwA")
                .mode(TravelMode.TRANSIT)
                .transitMode(TransitMode.BUS, TransitMode.SUBWAY, TransitMode.RAIL)
                .transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
                .units(com.google.maps.model.Unit.IMPERIAL)
                .await()
            routeSummaries = directions.routes.toList()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Origin: ${tokyo.latitude},${tokyo.longitude}")
        Text(text = "Destination: ${singapore.latitude},${singapore.longitude}")
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            routeSummaries?.forEach {route ->
                route.legs.forEach { leg ->
                    leg.steps.forEach { step ->
                        Text(text = step.htmlInstructions)
                    }
                }
            }
        }
    }

}