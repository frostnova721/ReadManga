package app.ice.readmanga.ui.navigator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.ice.readmanga.ui.pages.Home
import app.ice.readmanga.ui.pages.info.Info
import app.ice.readmanga.ui.pages.Library
import app.ice.readmanga.ui.pages.MainScreen
import app.ice.readmanga.ui.pages.read.Read
import app.ice.readmanga.ui.pages.Search
import app.ice.readmanga.ui.pages.Updates
import app.ice.readmanga.ui.pages.info.InfoSharedViewModel


@Composable
fun MainScreenBottomBarGraph(rootController: NavHostController, navController: NavHostController) {
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
        composable<Routes.InfoRoute> { bse ->
            val id = bse.toRoute<Routes.InfoRoute>().id
            val svm: InfoSharedViewModel = viewModel()
            Info(id = id, rootNavigator = navController, infoSharedViewModel = svm)
        }
        composable<Routes.ReadRoute> { bse ->
            val args = bse.toRoute<Routes.ReadRoute>()
            Read(
                rootNavController = navController,
                chapterId = args.chapterLink,
                chapterNumber = args.chapterNumber,
                id = args.id,
            )
        }
//        composable(
//            "info/{id}",
//        ) { navBackStackEntry ->
//            val args = requireNotNull(navBackStackEntry.arguments)
//            val id = args.getString("id")?.toInt()
//            val svm : InfoSharedViewModel = viewModel()
//            Info(id = id!!, navController, svm)
//        }
//        composable("read/{chapterId}/{chapterNumber}") { navBackStackEntry ->
//            val args = requireNotNull(navBackStackEntry.arguments)
//            val chapterId = args.getString("chapterId")
//            val chapterNumber = args.getString("chapterNumber")!!
//            Read(rootNavController = navController, chapterId!!, chapterNumber)
//        }
    }
}

private fun NavGraphBuilder.bottomBarGraph(
    rootController: NavHostController, barNavController: NavHostController
) {

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