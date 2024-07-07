package app.ice.readmanga.core.database.anilist

import app.ice.readmanga.types.AnilistSearchResult
import app.ice.readmanga.types.MangaTitle
import app.ice.readmanga.types.anilistResponses.AnilistSearchResponse
import app.ice.readmanga.utils.get
import app.ice.readmanga.utils.gqlRequest
import io.ktor.client.call.body
import org.json.JSONArray
import org.json.JSONObject

class Anilist {

    private val url = "https://graphql.anilist.co/"

    suspend fun search(query: String): List<AnilistSearchResult> {
        val gqlQuery = AnilistQueries().searchQuery(query)
        val res = gqlRequest(url, gqlQuery)
        val typed = res.body<AnilistSearchResponse>()
        val medias = typed.data?.page?.media ?: throw Exception("GOT MEDIA AS NULL WHILE SEARCH")

        val searchResults = mutableListOf<AnilistSearchResult>()

        medias.forEach { media ->
            searchResults.add(
                AnilistSearchResult(
                    id = media.id,
                    title = MangaTitle(
                        english = media.title?.english,
                        romaji = media.title?.romaji,
                    ),
                    rating = media.averageScore,
                    cover = media.cover.large
                ),
            )
        }


        return searchResults
//        return emptyList()
    }
}