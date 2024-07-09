package app.ice.readmanga.types.anilistResponses.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Characters(
    @SerialName("edges")
    val edges: List<CharacterEdges>?
)

@Serializable
data class CharacterEdges(
    @SerialName("node")
    val node: CharacterNode?,

    @SerialName("role")
    val role: String?
)

@Serializable
data class CharacterNode(
    @SerialName("name")
    val name: CharacterName?,

    @SerialName("image")
    val image: CharacterImage?

)

@Serializable
data class CharacterName(
    @SerialName("full")
    val full: String?,

    @SerialName("native")
    val native: String?,
)

@Serializable
data class CharacterImage(
    @SerialName("large")
    val large: String?
)
