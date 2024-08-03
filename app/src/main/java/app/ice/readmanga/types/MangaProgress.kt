package app.ice.readmanga.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MangaProgressList(
    val id: Int,
    val title: String,
    var read: Float?,
    val total: Float?,
    val cover: String,
) : Parcelable