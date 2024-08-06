package app.ice.readmanga.ui.navigator

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    object MainScreenRoute

    @Serializable
    data class InfoRoute(val id: Int)

    @Serializable
    data class ReadRoute(val chapterLink: String, val chapterNumber: String, val id: Int)
}