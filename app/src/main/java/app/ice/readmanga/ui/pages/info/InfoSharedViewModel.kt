package app.ice.readmanga.ui.pages.info

import androidx.lifecycle.ViewModel

class InfoSharedViewModel : ViewModel() {
    var title: String? = null
    var selectedChapterLink: String? = null
    var selectedChapterNumber: String? = null
}