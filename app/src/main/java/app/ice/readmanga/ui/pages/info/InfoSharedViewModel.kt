package app.ice.readmanga.ui.pages.info

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.types.MangaProgressList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class InfoSharedViewModel : ViewModel(), Parcelable {
    var title: String? = null
    var coverImage: String? = null
    var id: Int? = null

    private val _titleFoundInSource = MutableStateFlow<String?>(null)
    val titleFoundInSource: StateFlow<String?> get() = _titleFoundInSource

    fun updateFoundTitle(newTitle: String) {
        _titleFoundInSource.value = newTitle
    }

    private val _readChapters = MutableStateFlow<Float?>(null)
    val readChapters: StateFlow<Float?> get() = _readChapters

    fun updateReadChapters(chapter: Float) {
        _readChapters.value = chapter
    }

    var selectedChapterLink: String? = null
    var selectedChapterNumber: String? = null

    var selectedSource: String = MangaSources.MANGADEX
}