package app.ice.readmanga.types.anilistResponses

import app.ice.readmanga.types.anilistResponses.components.AnilistData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnilistSearchResponse(
    @SerialName("data")
    val data: AnilistData?
)
