package jp.nitech.edamame.extension

import com.google.android.gms.maps.model.LatLng

fun String.toLatLng(): LatLng {
    val (lat, lng) = split(",")
    return LatLng(lat.toDouble(), lng.toDouble())
}
