package app.ice.readmanga.core.providers

import app.ice.readmanga.types.ChaptersResult
import app.ice.readmanga.types.MangadexItem
import app.ice.readmanga.types.MangadexResponse
import app.ice.readmanga.types.SearchResult
import app.ice.readmanga.utils.get
import io.ktor.client.call.body

class MangaDex: Provider() {
    override suspend fun search(query: String): List<SearchResult> {
        val res =
            get("https://api.mangadex.org/manga?title=$query&limit=5&order[relevance]=desc&includes[]=cover_art").body<MangadexResponse>()
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

    override suspend fun getChapters(id: String): List<ChaptersResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getPages(chapterLink: String, quality: String): List<String>? {
        TODO("Not yet implemented")
    }
}