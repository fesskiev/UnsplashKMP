package com.unsplash.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.unsplash.android.utils.LogCompositions

@Composable
fun MapScreen(
    name: String?,
    country: String?,
    city: String?,
    latitude: Double,
    longitude: Double
) {

    LogCompositions("MapScreen")

    val latLng by remember {
        derivedStateOf { LatLng(latitude, longitude) }
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                title = name,
                state = MarkerState(position = latLng),
                snippet = "$country $city"
            )
        }
    }
}