package app.ice.readmanga.ui.pages

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.R
import app.ice.readmanga.ui.models.BottomNavigationBar
import app.ice.readmanga.ui.navigator.MainScreenBottomBarGraph
import app.ice.readmanga.ui.theme.Rubik


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: NavHostController) {

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
            if(titles[currentRoute] != "ReadManga")
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
            Box(modifier = if(titles[currentRoute] != "ReadManga")Modifier.padding(innerPadding) else Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                MainScreenBottomBarGraph(rootNavController, navController = navController)
            }

        }
    )
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}