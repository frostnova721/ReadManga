package app.ice.readmanga.types

import kotlinx.serialization.Serializable

@Serializable
data class MangaProgressList(
    val title: String,
    var read: Float?,
    val total: Float?,
    val cover: String,
)