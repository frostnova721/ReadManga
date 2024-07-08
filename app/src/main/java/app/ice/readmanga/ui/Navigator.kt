package app.ice.readmanga.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.ice.readmanga.ui.pages.Home
import app.ice.readmanga.ui.pages.Info
import app.ice.readmanga.ui.pages.Library
import app.ice.readmanga.ui.pages.MainScreen
import app.ice.readmanga.ui.pages.Search
import app.ice.readmanga.ui.pages.Updates
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Composable
fun RootNavigationgraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_screen") {
       composable(route = "main_screen") {
           MainScreen()
       }

        composable("info/{id}") {
            val id = it.arguments?.getInt("id")
            Info(id = id!!)
        }
    }
}

@Composable
fun BottomBarNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {


//    navigation<MainScreenGraph>(startDestination = HomeScreen) {
        composable(route = "home") {
            Home(navHostController = navController)
        }
        composable(route = "search") {
            Search(navHostController = navController)
        }
        composable(route = "updates") {
            Updates()
        }
        composable(route = "library") {
            Library()
        }
        composable("info/{id}") {
            val id = it.arguments?.getString("id")?.toInt()
            Info(id = id!!)
        }
//    }
    }

}

@Serializable
object MainScreenGraph

@Serializable
object HomeScreen

@Serializable
object SearchScreen

@Serializable
object UpdatesScreen

@Serializable
object LibraryScreen

@Serializable
data class InfoScreen(
    val id: Int
)