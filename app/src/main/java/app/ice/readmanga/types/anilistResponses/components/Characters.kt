package app.ice.readmanga.types.anilistResponses.components

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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

@Parcelize
@Serializable
data class CharacterName(
    @SerialName("full")
    val full: String?,

    @SerialName("native")
    val native: String?,
) : Parcelable

@Serializable
data class CharacterImage(
    @SerialName("large")
    val large: String?
)
