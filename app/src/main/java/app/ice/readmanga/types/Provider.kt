package app.ice.readmanga.types

data class SearchResult(
    val id: String,
    val title: String,
    val cover: String,
)

data class Chapters(
    val chapter: String,
    val link: String,
)

data class ChaptersResult(
    val lang: String,
    val chapters: List<Chapters>
)