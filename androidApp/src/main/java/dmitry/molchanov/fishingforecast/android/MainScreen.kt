package dmitry.molchanov.fishingforecast.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dmitry.molchanov.fishingforecast.android.ui.NavItem
import dmitry.molchanov.fishingforecast.android.ui.Screens
import dmitry.molchanov.fishingforecast.android.ui.map.MapScreen
import dmitry.molchanov.fishingforecast.android.ui.profile.ProfileScreen
import dmitry.molchanov.fishingforecast.android.ui.setting.ForecastSettingsList

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
            startDestination = Screens.MAP.label,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.MAP.label) {
                MapScreen(vm)
            }
            composable(Screens.PROFILE.label) {
                ProfileScreen(vm)
            }
            composable(Screens.FORECAST_SETTINGS.label) {
                ForecastSettingsList()
            }
        }
    }

}