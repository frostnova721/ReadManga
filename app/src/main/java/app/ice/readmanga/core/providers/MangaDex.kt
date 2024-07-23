package app.ice.readmanga.core.providers

import app.ice.readmanga.types.Chapters
import app.ice.readmanga.types.ChaptersResult
import app.ice.readmanga.types.MangaDexPagesResponse
import app.ice.readmanga.types.MangadexItem
import app.ice.readmanga.types.MangadexResponse
import app.ice.readmanga.types.SearchResult
import app.ice.readmanga.utils.get
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class MangaDex : Provider() {
    override suspend fun search(query: String): List<SearchResult> {
        val encodedQuery = withContext(Dispatchers.IO) {
            URLEncoder.encode(query, "Utf-8")
        }
        val res =
            get("https://api.mangadex.org/manga?title=$encodedQuery&limit=5&order[relevance]=desc&includes[]=cover_art").body<MangadexResponse>()
        val data: List<MangadexItem> = res.data

        val searchResults = mutableListOf<SearchResult>()

        data.forEach { it ->
            var cover: String? = null;
            it.relationships?.forEach { rel ->
                if (rel.type == "cover_art") {
                    cover =
                        "https://mangadex.org/covers/${it.id}/${rel.attributes?.fileName}"
                }
            }
            searchResults.add(
                SearchResult(
                    id = it.id,
                    cover = cover ?: "",
                    title = (it.attributes?.title?.en ?: it.attributes?.altTitles?.get(0)?.get("en")
                    ?: it.attributes?.altTitles?.get(0)?.get("ja")).toString() ?: ""
                )
            )
        }

        return searchResults;
    }

    private suspend fun getRawChapterList(
        id: String,
        offset: Int,
        total: Int?
    ): List<MangadexItem> {
        if (total != null && offset >= total) return emptyList()
        val url =
            "https://api.mangadex.org/manga/${id}/feed?limit=96&order[volume]=desc&order[chapter]=desc&offset=${offset}&translatedLanguage[]=en"
        val res = get(url).body<MangadexResponse>()
        return res.data + getRawChapterList(id, offset + 96, res.total)
    }

    override suspend fun getChapters(id: String): List<ChaptersResult> {
//        val url = "https://api.mangadex.org/manga/$id"
//        val res = get(url).body<MangadexResponse>().data
        val rawChapterData = getRawChapterList(id, 0, null)

        val chapters = mutableListOf<Chapters>()
        println(id)

        for (ch in rawChapterData) {
            chapters.add(
                Chapters(
                    chapter = ch.attributes?.chapter ?: "",
                    link = ch.id
                )
            )
        }
        return listOf(ChaptersResult(lang = "en", chapters = chapters))
    }

    override suspend fun getPages(chapterLink: String, quality: String): List<String>? {
        val url = "https://api.mangadex.org/at-home/server/${chapterLink}"
        val res = get(url).body<MangaDexPagesResponse>()
        val links = mutableListOf<String>()
        res.chapter.data.forEach {
            links.add("${res.baseUrl}/data/${res.chapter.hash}/$it")
        }

        return links
    }
}