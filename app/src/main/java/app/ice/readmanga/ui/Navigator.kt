package app.ice.readmanga.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.ui.pages.Home
import app.ice.readmanga.ui.pages.Library
import app.ice.readmanga.ui.pages.Search
import app.ice.readmanga.ui.pages.Updates

//@Composable
//fun Navigator(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = "Home") {
//        composable("Home") {
//            Home(navHostController = navController)
//        }
//        composable("Page2") {
//            Page2(navHostController = navController)
//        }
//    }
//}

@Composable
fun BottomNavigationBarGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Home(navHostController = navController)
        }
        composable("search") {
            Search(navHostController = navController)
        }
        composable("updates") {
            Updates()
        }
        composable("library") {
            Library()
        }
    }
}