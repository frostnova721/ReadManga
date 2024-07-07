package app.ice.readmanga.types.anilistResponses.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnilistMedia(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: AnilistMediaTitle?,

    @SerialName("coverImage")
    val cover: AnilistMediaCoverImage,

    @SerialName("averageScore")
    val averageScore: Int?
)

@Serializable
data class AnilistMediaCoverImage(
    @SerialName("large")
    val large: String,
)

@Serializable
data class AnilistMediaTitle(
    @SerialName("english")
    val english: String?,

    @SerialName("romaji")
    val romaji: String?
)