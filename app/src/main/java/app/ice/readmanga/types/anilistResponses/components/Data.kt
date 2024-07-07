package app.ice.readmanga.types.anilistResponses.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnilistData(
    @SerialName("Page")
    val page: AnilistPage?
)