package app.ice.readmanga.types

import android.os.Parcel
import android.os.Parcelable
import app.ice.readmanga.types.anilistResponses.components.AnilistFuzzyDateFormat
import app.ice.readmanga.types.anilistResponses.components.CharacterName
import app.ice.readmanga.types.anilistResponses.components.Characters
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaTitle(
    val english: String?,
    val romaji: String?
): Parcelable

@Parcelize
data class CharactersSimplified(
    val name: CharacterName,
    val role: String,
    val image: String?,
) : Parcelable

@Parcelize
data class RecommendationSimplified(
    val id: Int,
    val title: MangaTitle,
    val type: String,
    val rating: Int?,
    val cover: String,
): Parcelable

@Parcelize
data class AnilistSearchResult(
    val id: Int,
    val title: MangaTitle,
    val cover: String,
    val rating: Int?,
): Parcelable

@Parcelize
data class AnilistInfoResult(
    val id: Int,
    val title: MangaTitle,
    val cover: String,
    val banner: String?,
    val rating: Int?,
    val synonyms: List<String>?,
    val genres: List<String>?,
    val description: String?,
    val source: String?,
    val type: String,
    val chapters: Int?,
    val status: String?,
    val tags: List<String>?,
    val startDate: AnilistFuzzyDateFormat?,
    val endDate: AnilistFuzzyDateFormat?,
    val characters: List<CharactersSimplified>?,
    val recommenations: List<RecommendationSimplified>?,
    val relations: List<RecommendationSimplified>?,
) : Parcelable

@Parcelize
data class AnilistTrendingResult(
    val id: Int,
    val title: MangaTitle,
    val genres: List<String>?,
    val rating: Int?,
    val banner: String?,
    val cover: String,
    val chapters: Int?,
): Parcelable
