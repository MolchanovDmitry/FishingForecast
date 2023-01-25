package dmitry.molchanov.fishingforecast.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import dmitry.molchanov.domain.mapper.string
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Result

enum class NavItem(val label: String, val icon: ImageVector, val destination: Screen) {
    MAP(label = "Карта", icon = Icons.Filled.Map, destination = Screen.Map),
    PROFILE(label = "Профиль", icon = Icons.Default.People, destination = Screen.Profile),
    FORECAST_SETTINGS(
        label = "Настройки",
        icon = Icons.Default.Settings,
        destination = Screen.ForecastSettings
    ),
    WEATHER_LIST(
        label = "Список",
        icon = Icons.Default.List,
        destination = Screen.WeatherList
    ),
    RESULTS(
        label = "Результаты",
        icon = Icons.Default.ThumbUp,
        destination = Screen.Results
    )
}

sealed class Screen(val label: String) {
    object Map : Screen(label = "Map")
    object Profile : Screen(label = "Profile")
    object ForecastSettings : Screen(label = "Forecast settings")
    object WeatherList : Screen(label = "Weather list")
    object Weather : Screen(label = "Weather?mapPoint={mapPoint}") {
        fun route(mapPoint: MapPoint) = "Weather?mapPoint=${mapPoint.string()}"
    }

    object Results : Screen(label = "Results")
    object ResultDetails : Screen(label = "ResultDetail?result={result}") {
        fun route(result: Result) = "ResultDetail?result=${result.string()}"
    }
}