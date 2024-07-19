package app.ice.readmanga.core.source_handler

import app.ice.readmanga.core.providers.MangaDex
import app.ice.readmanga.core.providers.MangaReader
import app.ice.readmanga.core.providers.Provider
import app.ice.readmanga.types.ChaptersResult
import app.ice.readmanga.types.SearchResult

class SourceHandler(private val source: String) {
    private fun getSource(source: String): Provider {
        when(source) {
            "manga_reader" -> return MangaReader()
            "mangadex" -> return MangaDex()
        }

        throw Exception("UNKNOWN SOURCE")
    }

    suspend fun search(query: String): List<SearchResult> {
        val src = getSource(source)
        val res = src.search(query)
        return res;
    }

    suspend fun getChapters(id: String): List<ChaptersResult> {
        val src = getSource(source)
        val res = src.getChapters(id)
        return res
    }

    suspend fun getPages(chapterLink: String): List<String>? {
        val src = getSource(source)
        val res = src.getPages(chapterLink)
        return res
    }
}