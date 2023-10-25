package jp.nitech.edamame.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng

object LocationUtil {

    /**
     * Returns current latlng.
     * If the system cannot get current latlng, returns null.
     */
    fun getCurrentLatLng(
        context: Context,
    ): LatLng? {
        // Check permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        // Get current location
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = lm.getProviders(true)
        var location: Location? = null
        providers.forEach { provider ->
            location = lm.getLastKnownLocation(provider)
        }

        // Move location of the map to current location
        return location?.let {
            LatLng(it.latitude, it.longitude)
        }
    }
}