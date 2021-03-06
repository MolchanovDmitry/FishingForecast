package dmitry.molchanov.fishingforecast.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dmitry.molchanov.fishingforecast.android.ui.NavItem
import dmitry.molchanov.fishingforecast.android.ui.Screen
import dmitry.molchanov.fishingforecast.android.ui.map.MapScreen
import dmitry.molchanov.fishingforecast.android.ui.profile.ProfileScreen
import dmitry.molchanov.fishingforecast.android.ui.result.ResultScreen
import dmitry.molchanov.fishingforecast.android.ui.setting.ForecastSettingsList
import dmitry.molchanov.fishingforecast.android.ui.weather.WeatherDebugScreen
import dmitry.molchanov.fishingforecast.android.ui.weather.WeatherScreen
import dmitry.molchanov.fishingforecast.model.toMapPoint

@Composable
fun MainScreen(vm: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavItem.values().forEachIndexed { index, navItem ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = "Back button"
                            )
                        },
                        label = { Text(navItem.label) },
                        selected = currentDestination?.route == navItem.label,
                        onClick = {
                            navController.navigate(navItem.destination.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.label,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Map.label) {
                MapScreen(vm)
            }
            composable(Screen.Profile.label) {
                ProfileScreen(vm)
            }
            composable(Screen.ForecastSettings.label) {
                ForecastSettingsList(vm)
            }
            composable(Screen.WeatherList.label) {
                WeatherDebugScreen(vm) {
                    navController.navigate(Screen.Weather.route(mapPoint = it))
                }
            }
            composable(Screen.Weather.label) {
                val mapPoint = it.arguments?.getString("mapPoint")?.toMapPoint()
                    ?: return@composable
                WeatherScreen(mapPoint, vm.state.collectAsState().value.forecastSettings)
            }
            composable(Screen.Results.label){
                ResultScreen()
            }
        }
    }

}