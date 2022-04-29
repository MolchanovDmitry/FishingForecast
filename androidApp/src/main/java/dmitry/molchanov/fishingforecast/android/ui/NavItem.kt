package dmitry.molchanov.fishingforecast.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.string

enum class NavItem(val label: String, val icon: ImageVector, val destination: Screen) {
    MAP(label = "Map", icon = Icons.Filled.Map, destination = Screen.Map),
    PROFILE(label = "Profile", icon = Icons.Default.People, destination = Screen.Profile),
    FORECAST_SETTINGS(
        label = "Forecast settings",
        icon = Icons.Default.Settings,
        destination = Screen.ForecastSettings
    ),
    WEATHER_LIST(
        label = "Weather list",
        icon = Icons.Default.List,
        destination = Screen.WeatherList
    ),
}

sealed class Screen(val label: String) {
    object Map : Screen(label = "Map")
    object Profile : Screen(label = "Profile")
    object ForecastSettings : Screen(label = "Forecast settings")
    object WeatherList : Screen(label = "Weather list")
    object Weather : Screen(label = "Weather?mapPoint={mapPoint}") {
        fun route(mapPoint: MapPoint) = "Weather?mapPoint=${mapPoint.string()}"
    }
}