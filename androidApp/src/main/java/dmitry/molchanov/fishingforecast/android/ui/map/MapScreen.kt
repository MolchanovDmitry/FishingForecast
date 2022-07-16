package dmitry.molchanov.fishingforecast.android.ui.map

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
import dmitry.molchanov.fishingforecast.android.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel


@Composable
fun MapScreen() {
    val vm by viewModel<MapViewModel>()
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