package app.ice.readmanga.ui.pages.info

import androidx.lifecycle.ViewModel
import app.ice.readmanga.core.source_handler.MangaSources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InfoSharedViewModel : ViewModel() {
    var title: String? = null

    private val _titleFoundInSource = MutableStateFlow<String?>(null)
    val titleFoundInSource: StateFlow<String?> get() = _titleFoundInSource

    fun updateFoundTitle(newTitle: String) {
        _titleFoundInSource.value = newTitle
    }

    var selectedChapterLink: String? = null
    var selectedChapterNumber: String? = null

    var selectedSource: String = MangaSources.MANGADEX
}