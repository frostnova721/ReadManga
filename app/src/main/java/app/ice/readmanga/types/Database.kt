package app.ice.readmanga.types

import app.ice.readmanga.types.anilistResponses.components.AnilistFuzzyDateFormat
import app.ice.readmanga.types.anilistResponses.components.CharacterName
import app.ice.readmanga.types.anilistResponses.components.Characters

data class MangaTitle(
    val english: String?,
    val romaji: String?
)

data class CharactersSimplified(
    val name: CharacterName,
    val role: String,
    val image: String?,
)

data class RecommendationSimplified(
    val id: Int,
    val title: MangaTitle,
    val type: String,
    val rating: Int?,
    val cover: String,
)

data class AnilistSearchResult(
    val id: Int,
    val title: MangaTitle,
    val cover: String,
    val rating: Int?,
)

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
)
