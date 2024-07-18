package app.ice.readmanga.types

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangadexResponse(
    @SerialName("data")
    val data: List<MangadexItem>
)

@Serializable
data class MangadexItem(
    @SerialName("id")
    val id: String,

    @SerialName("attributes")
    val attributes: Attributes?,

    @SerialName("relationships")
    val relationships: List<Relationship>?
)

@Serializable
data class Attributes(
    @SerialName("title")
    val title: Title,

    @SerialName("altTitles")
    val altTitles: List<Map<String, String>>,

    @SerialName("tags")
    val tags: List<Tag>
)

@Serializable
data class Title(
    @SerialName("en")
    val en: String
)

@Serializable
data class Tag(
    @SerialName("attributes")
    val attributes: TagAttributes?
)

@Serializable
data class TagAttributes(
    val name: Name
)

@Serializable
data class Name(
    val en: String
)

@Serializable
data class Relationship(
    val type: String,

    @SerialName("attributes")
    val attributes: RelationshipAttributes? = null
)

@Serializable
data class RelationshipAttributes(
    @SerialName("fileName")
    val fileName: String?
)