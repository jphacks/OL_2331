package jp.nitech.edamame.extension

fun com.google.maps.model.LatLng.convertLib(): com.google.android.gms.maps.model.LatLng {
    return com.google.android.gms.maps.model.LatLng(
        this.lat,
        this.lng,
    )
}

fun com.google.android.gms.maps.model.LatLng.convertLib(): com.google.maps.model.LatLng {
    return com.google.maps.model.LatLng(
        this.latitude,
        this.longitude,
    )
}

fun com.google.android.gms.maps.model.LatLng.formatCommaSplit(): String {
    return "${this.latitude},${this.longitude}"
}