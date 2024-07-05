package app.ice.readmanga.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.R
import app.ice.readmanga.ui.BottomNavigationBarGraph
import app.ice.readmanga.ui.models.BottomNavigationBar
import app.ice.readmanga.ui.theme.Rubik

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController();
    
    val currentRoute = getCurrentRoute(navController = navController)

    val titles = mapOf(
        "home" to "ReadManga",
        "search" to "Search",
        "updates" to "Updates",
        "library" to "Library"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row {
                    Text(text = titles[currentRoute] ?: "ReadManga", fontFamily = Rubik, fontSize = 26.sp )
                }
            })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                BottomNavigationBarGraph(navController = navController)
            }

        }
    )
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}