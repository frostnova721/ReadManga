package app.ice.readmanga.types.anilistResponses.components

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnilistMedia(
    //only exception of this being null is for info query
    @SerialName("id")
    val id: Int? = null,

    @SerialName("title")
    val title: AnilistMediaTitle? = null,

    @SerialName("coverImage")
    val cover: AnilistMediaCoverImage,

    @SerialName("averageScore")
    val averageScore: Int? = null,

    @SerialName("bannerImage")
    val banner: String? = null,

    @SerialName("synonyms")
    val synonyms: List<String>? = null,

    @SerialName("genres")
    val genres: List<String>? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("source")
    val source: String? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("chapters")
    val chapters: Int? = null,

    @SerialName("tags")
    val tags: List<AnilistTags>? = null,

    @SerialName("startDate")
    val startDate: AnilistFuzzyDateFormat? = null,

    @SerialName("endDate")
    val endDate: AnilistFuzzyDateFormat? = null,

    @SerialName("status")
    val status: String? = null,

    @SerialName("characters")
    val characters: Characters? = null,

    @SerialName("recommendations")
    val recommendations: Recommendations? = null,

    @SerialName("relations")
    val relations: Relations? = null,
)

@Parcelize
@Serializable
data class AnilistFuzzyDateFormat(
    @SerialName("year")
    val year: Int?,

    @SerialName("month")
    val month: Int?,

    @SerialName("day")
    val day: Int?
) : Parcelable

@Serializable
data class AnilistTags(
    @SerialName("name")
    val name: String,
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