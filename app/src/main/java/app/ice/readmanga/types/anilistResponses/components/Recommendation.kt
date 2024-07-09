package app.ice.readmanga.types.anilistResponses.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recommendations(
    @SerialName("nodes")
    val nodes: List<RecommendationNode>?
)

@Serializable
data class RecommendationNode(
    @SerialName("mediaRecommendation")
    val mediaRecommendation: MediaRecommendation?
)

@Serializable
data class MediaRecommendation(
    @SerialName("id")
    val id: Int?,

    @SerialName("type")
    val type: String?,

    @SerialName("averageScore")
    val averageScore: Int?,

    @SerialName("title")
    val title: RecommendationTitle?,

    @SerialName("coverImage")
    val coverImage: RecommendationImage?
)

@Serializable
data class RecommendationTitle(
    @SerialName("romaji")
    val romaji: String?,

    @SerialName("english")
    val english: String?
)

@Serializable
data class RecommendationImage(
    @SerialName("large")
    val large: String?
)
