package dmitry.molchanov.fishingforecast.ui.map

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import dmitry.molchanov.fishingforecast.ui.common.rememberMapViewWithLifecycle
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
    val isOpedDialog = remember { mutableStateOf(false) }
    var longClickPoint by remember { mutableStateOf(Pair(0.0, 0.0)) }
    val tapListener = remember {
        object : InputListener {
            override fun onMapTap(p0: Map, p1: Point) = Unit

            override fun onMapLongTap(map: Map, point: Point) {
                longClickPoint = point.latitude to point.longitude
                isOpedDialog.value = true
            }
        }
    }

    Box {
        AndroidView(factory = {
            mapView.apply {
                mapView.map.isZoomGesturesEnabled = true
                state.value.mapPoints.lastOrNull()?.let {
                    mapView.map.move(
                        CameraPosition(
                            Point(it.latitude, it.longitude),
                            14.0f,
                            0.0f,
                            0.0f
                        )
                    )
                }
                mapView.map.addInputListener(tapListener)
            }
        }, update = {
            state.value.mapPoints.forEach { mapPoint ->
                mapView.map.mapObjects.addPlacemark(Point(mapPoint.latitude, mapPoint.longitude))
            }
            state.value.mapPoints.lastOrNull()?.let { mapPoint ->
                mapView.map.move(
                    CameraPosition(
                        Point(mapPoint.latitude, mapPoint.longitude),
                        14.0f, 0.0f, 0.0f
                    )
                )
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