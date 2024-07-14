package app.ice.readmanga.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchResult(
    val id: String,
    val title: String,
    val cover: String,
)

@Parcelize
data class Chapters(
    val chapter: String,
    val link: String,
): Parcelable

data class ChaptersResult(
    val lang: String,
    val chapters: List<Chapters>
)