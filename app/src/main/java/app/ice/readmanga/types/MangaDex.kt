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
    val id: String,
    @SerialName("attributes")
    val attributes: Attributes?,
    @SerialName("relationships")
    val relationships: List<Relationship>?
)

@Serializable
data class Attributes(
    val title: Title,
    val altTitles: List<Map<String, String>>,
    val tags: List<Tag>
)

@Serializable
data class Title(
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
    val attributes: RelationshipAttributes?
)

@Serializable
data class RelationshipAttributes(
    val fileName: String
)