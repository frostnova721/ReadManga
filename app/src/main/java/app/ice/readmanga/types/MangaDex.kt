package app.ice.readmanga.types

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MangadexResponse(
    @SerialName("total")
    val total: Int? = null,

    @SerialName("offset")
    val offset: Int? = null,

    @SerialName("data")
    val data: List<MangadexItem>
)

@Serializable
data class MangaDexPagesResponse(
    @SerialName("chapter")
    val chapter: MangaDexPagesChapters,

    @SerialName("baseUrl")
    val baseUrl: String,
)

@Serializable
data class MangaDexPagesChapters(
    @SerialName("hash")
    val hash: String,

    @SerialName("data")
    val data: List<String>,

    @SerialName("dataSaver")
    val dataSaver: List<String>,
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
    @Serializable(with = TitleSerializer::class)
    @SerialName("title")
    val title: Title? = null,

    @SerialName("altTitles")
    val altTitles: List<Map<String, String>>? = null,

    @SerialName("tags")
    val tags: List<Tag>? = null,

    @SerialName("chapter")
    val chapter: String? = null,

    @SerialName("volume")
    val volume: String? = null,

//    @SerialName("pages")
//    val pages: Int,
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
    val type: String? = null,

    @SerialName("attributes")
    val attributes: RelationshipAttributes? = null
)

@Serializable
data class RelationshipAttributes(
    @SerialName("fileName")
    val fileName: String?
)

object TitleSerializer : KSerializer<Title> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Title") {
        element<String>("en")
    }

    override fun serialize(encoder: Encoder, value: Title) {
        encoder.encodeString(value.en)
    }

    override fun deserialize(decoder: Decoder): Title {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonInput for deserialization")
        val jsonElement = input.decodeJsonElement()

        return when (jsonElement) {
            is JsonObject -> {
                val en = jsonElement["en"]?.jsonPrimitive?.content ?: throw SerializationException("Missing 'en' field")
                Title(en)
            }
            is JsonPrimitive -> Title(jsonElement.content)
            else -> throw SerializationException("Unexpected JSON type: ${jsonElement::class}")
        }
    }
}