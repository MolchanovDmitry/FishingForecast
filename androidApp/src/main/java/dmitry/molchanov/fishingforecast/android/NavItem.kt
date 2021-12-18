package dmitry.molchanov.fishingforecast.android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavItem(val label: String, val icon: ImageVector) {
    MAP(label = "Map", icon = Icons.Filled.Map),
    PROFILE(label = "Profile", icon = Icons.Default.People)
}

enum class Screens(val label: String){
    MAP(label = "Map"),
    PROFILE(label = "Profile")
}