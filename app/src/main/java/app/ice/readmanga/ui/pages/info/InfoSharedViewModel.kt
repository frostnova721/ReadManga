package app.ice.readmanga.ui.pages.info

import androidx.lifecycle.ViewModel
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.types.Chapters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InfoSharedViewModel : ViewModel() {
    var title: String? = null
    var coverImage: String? = null
    var id: Int? = null

    private val _source = MutableStateFlow<String>(MangaSources.MANGADEX)
    val source: StateFlow<String> get() = _source
    fun changeSource(mangaSource: String) {
        _source.value = mangaSource
    }

    //title that was found in the source
    private val _titleFoundInSource = MutableStateFlow<String?>(null)
    val titleFoundInSource: StateFlow<String?> get() = _titleFoundInSource

    fun updateFoundTitle(newTitle: String) {
        _titleFoundInSource.value = newTitle
    }

    //progress
    private val _readChapters = MutableStateFlow<Float?>(null)
    val readChapters: StateFlow<Float?> get() = _readChapters

    fun updateReadChapters(chapter: Float) {
        _readChapters.value = chapter
    }

    //chapters
    private val _chapterList = MutableStateFlow<List<Chapters?>?>(null)
    val chapterList: StateFlow<List<Chapters?>?> get() = _chapterList

    fun addChapters(chapters: List<Chapters?>?) {
        _chapterList.value = chapters
    }

    var selectedChapterLink: String? = null
    var selectedChapterNumber: String? = null

    fun clearViewModel() {
        _titleFoundInSource.value = null
        _chapterList.value = null
        selectedChapterNumber = null
        selectedChapterLink = null
    }
}