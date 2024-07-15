package app.ice.readmanga.ui.navigator

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.ice.readmanga.ui.pages.Home
import app.ice.readmanga.ui.pages.info.Info
import app.ice.readmanga.ui.pages.Library
import app.ice.readmanga.ui.pages.MainScreen
import app.ice.readmanga.ui.pages.Read
import app.ice.readmanga.ui.pages.Search
import app.ice.readmanga.ui.pages.Updates
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun MainScreenBottomBarGraph(rootController: NavHostController,navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {

        bottomBarGraph(rootController = rootController, barNavController = navController)
    }

}

@Composable
fun ReadMangaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main_screen",
    ) {
        composable(route = "main_screen") {
            MainScreen(navController)
        }
        composable(
            "info/{id}",
        ) { navBackStackEntry ->
            val args = requireNotNull(navBackStackEntry.arguments)
            val id = args.getString("id")?.toInt()
            Info(id = id!!, navController)
        }
        composable("read/{chapterId}/{chapterNumber}") { navBackStackEntry ->
            val args = requireNotNull(navBackStackEntry.arguments)
            val chapterId = args.getString("chapterId")
            val chapterNumber = args.getString("chapterNumber")!!
            Read(rootNavController = navController, chapterId!!, chapterNumber)
        }
    }
}

private fun NavGraphBuilder.bottomBarGraph(rootController: NavHostController,barNavController: NavHostController) {

        composable(route = "home") {
            Home(rootController, barNavController)
        }
        composable(route = "search") {
            Search(rootController, barNavController)
        }
        composable(route = "updates") {
            Updates(rootController, barNavController)
        }
        composable(route = "library") {
            Library(rootController, barNavController)
        }

}