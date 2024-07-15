package app.ice.readmanga.core.providers

import android.content.Context
import app.ice.readmanga.types.ChaptersResult
import app.ice.readmanga.types.SearchResult

abstract class Provider() {
    abstract suspend fun search(query: String): List<SearchResult>

    abstract suspend fun getChapters(id: String): List<ChaptersResult>

    abstract suspend fun getPages(chapterLink: String, quality:String = "medium"): List<String>?
}