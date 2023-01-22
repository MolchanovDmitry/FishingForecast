package dmitry.molchanov.fishingforecast.android.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import dmitry.molchanov.fishingforecast.android.ui.common.rememberMapViewWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen() {
    val vm = koinViewModel<MapViewModel>()
    val state = vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        MapView(state, vm)
    }
}

@Composable
fun MapView(state: State<MapViewState>, vm: MapViewModel) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    var map by remember { mutableStateOf<GoogleMap?>(null) }
    val isOpedDialog = remember { mutableStateOf(false) }
    var longClickPoint by remember { mutableStateOf(Pair(0.0, 0.0)) }

    Box {
        AndroidView(factory = {
            coroutineScope.launch {
                map = mapView.awaitMap()
                map?.uiSettings?.isZoomControlsEnabled = true
                map?.setOnMapLongClickListener {
                    longClickPoint = it.latitude to it.longitude
                    isOpedDialog.value = true
                }
                map?.setOnMapClickListener(null)
                state.value.mapPoints.lastOrNull()?.let {
                    val position = LatLng(it.latitude, it.longitude)
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 6f))
                }
            }
            mapView
        }, update = {
            state.value.mapPoints.forEach { mapPoint ->
                val point = LatLng(mapPoint.latitude, mapPoint.longitude)
                val markerOptions = MarkerOptions()
                    .position(point)
                map?.addMarker(markerOptions)
            }
        })
    }
    CreateMapPointDialog(
        openDialog = isOpedDialog,
        profiles = state.value.profiles,
        createMapPoint = { title, profile ->
            vm.onAction(
                SavePoint(
                    title = title,
                    profile = profile,
                    latitude = longClickPoint.first,
                    longitude = longClickPoint.second
                )
            )
        }
    )
}