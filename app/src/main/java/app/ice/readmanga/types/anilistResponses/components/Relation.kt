package app.ice.readmanga.types.anilistResponses.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Relations(
    @SerialName("edges")
    val edges: List<RelationEdge>?
)

@Serializable
data class RelationEdge(
    @SerialName("relationType")
    val relationType: String?,

    @SerialName("node")
    val node: RelationNode?
)

@Serializable
data class RelationNode(
    @SerialName("id")
    val id: Int?,

    @SerialName("type")
    val type: String?,

    @SerialName("title")
    val title: RelationTitle?,

    @SerialName("averageScore")
    val averageScore: Int?,

    @SerialName("coverImage")
    val coverImage: RelationImage?
)

@Serializable
data class RelationTitle(
    @SerialName("romaji")
    val romaji: String?,

    @SerialName("english")
    val english: String?
)

@Serializable
data class RelationImage(
    @SerialName("large")
    val large: String?
)
