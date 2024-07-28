package app.ice.readmanga.ui.models

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import compose.icons.FeatherIcons
import compose.icons.feathericons.File
import compose.icons.feathericons.Folder
import compose.icons.feathericons.Home
import compose.icons.feathericons.PlusSquare
import compose.icons.feathericons.Search
import compose.icons.feathericons.Speaker

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationBarItem("Home", "home", FeatherIcons.Home),
        BottomNavigationBarItem("Search", "search", FeatherIcons.Search),
        BottomNavigationBarItem(
            "Updates",
            "updates",
            FeatherIcons.PlusSquare
        ),
        BottomNavigationBarItem("Library", "library", FeatherIcons.Folder)
    )

    var selectedItem by remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { route ->
            selectedItem = items.indexOfFirst { it.route == route }
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                        selectedItem = index
                    }
                }, icon = {
                    Icon(item.icon, contentDescription = item.label, modifier = Modifier.width(23.dp))
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}

data class BottomNavigationBarItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)