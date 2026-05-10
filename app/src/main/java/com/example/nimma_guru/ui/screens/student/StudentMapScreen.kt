package com.example.nimma_guru.ui.screens.student

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.nimma_guru.ui.viewmodel.GuruViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun StudentMapScreen(viewModel: GuruViewModel) {
    val gurus by viewModel.gurus.collectAsState()
    
    // Default village center (Example: a village in Karnataka)
    val villageCenter = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(villageCenter, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        gurus.forEach { guru ->
            if (guru.latitude != null && guru.longitude != null) {
                Marker(
                    state = MarkerState(position = LatLng(guru.latitude, guru.longitude)),
                    title = guru.name,
                    snippet = guru.skills.joinToString(", ")
                )
            }
        }
    }
}
