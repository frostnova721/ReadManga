package app.ice.readmanga.types

data class MangaTitle(
    val english: String?,
    val romaji: String?
)

data class AnilistSearchResult(
    val id: Int,
    val title: MangaTitle,
    val cover: String,
    val rating: Int?,
)
