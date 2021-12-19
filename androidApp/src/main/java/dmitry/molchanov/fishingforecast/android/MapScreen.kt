package dmitry.molchanov.fishingforecast.android

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch


@Composable
fun MapScreen(vm: MainViewModel) {
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
fun MapView(state: State<MainViewState>, vm: MainViewModel) {
    val mapView = rememberMapViewWithLifecycle()
    var map by remember { mutableStateOf<GoogleMap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    Box {
        AndroidView(factory = {
            coroutineScope.launch {
                map = mapView.awaitMap()
                map?.uiSettings?.isZoomControlsEnabled = true
                map?.setOnMapLongClickListener {
                    vm.onEvent(
                        SavePoint(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    )
                }
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
                    .title("Sydney Opera House")
                    .position(point)
                map?.addMarker(markerOptions)
            }
        })
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Event.ON_CREATE -> mapView.onCreate(Bundle())
                Event.ON_START -> mapView.onStart()
                Event.ON_RESUME -> mapView.onResume()
                Event.ON_PAUSE -> mapView.onPause()
                Event.ON_STOP -> mapView.onStop()
                Event.ON_DESTROY -> mapView.onDestroy()
                else -> error("Uncatched lifecycle event exception")
            }
        }
    }