package app.ice.readmanga.core.database.anilist

import app.ice.readmanga.types.AnilistInfoResult
import app.ice.readmanga.types.AnilistSearchResult
import app.ice.readmanga.types.CharactersSimplified
import app.ice.readmanga.types.MangaTitle
import app.ice.readmanga.types.RecommendationSimplified
import app.ice.readmanga.types.anilistResponses.AnilistResponse
import app.ice.readmanga.types.anilistResponses.components.CharacterName
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
        val medias = res?.data?.page?.media ?: throw Exception("GOT MEDIA AS NULL WHILE SEARCH")

        val searchResults = mutableListOf<AnilistSearchResult>()

        medias.forEach { media ->
            searchResults.add(
                AnilistSearchResult(
                    id = media.id!!,
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
    }

    suspend fun getInfo(id: Int): AnilistInfoResult? {
        val gqlQuery = AnilistQueries().infoQuery(id)
        val res = gqlRequest(url, gqlQuery)
        val media = res?.data?.page?.media?.get(0) ?: return null;

        val charactersResponse = media.characters
        val recommendationsResponse = media.recommendations
        val relationsResponse = media.relations

        val characters = mutableListOf<CharactersSimplified>()
        val recommendations = mutableListOf<RecommendationSimplified>()
        val relations = mutableListOf<RecommendationSimplified>()

        if (charactersResponse?.edges != null) {
            charactersResponse.edges.forEach { chara ->
                characters.add(
                    CharactersSimplified(
                        name = CharacterName(
                            full = chara.node?.name?.full,
                            native = chara.node?.name?.native
                        ),
                        image = chara.node?.image?.large,
                        role = chara.role ?: "unknown",
                    )
                )
            }
        }

        if (relationsResponse != null) {
            relationsResponse.edges?.forEach { item ->
                if (item.node != null) {
                    relations.add(
                        RecommendationSimplified(
                            id = item.node.id!!,
                            title = MangaTitle(
                                english = item.node.title?.english,
                                romaji = item.node.title?.romaji,
                            ),
                            cover = item.node.coverImage!!.large!!,
                            rating = item.node.averageScore,
                            type = item.node.type!!
                        )
                    )
                }
            }
        }

        if (recommendationsResponse != null) {
            recommendationsResponse.nodes?.forEach { item ->
                if (item.mediaRecommendation != null) {
                    relations.add(
                        RecommendationSimplified(
                            id = item.mediaRecommendation.id!!,
                            title = MangaTitle(
                                english = item.mediaRecommendation.title?.english,
                                romaji = item.mediaRecommendation.title?.romaji,
                            ),
                            cover = item.mediaRecommendation.coverImage!!.large!!,
                            rating = item.mediaRecommendation.averageScore,
                            type = item.mediaRecommendation.type!!
                        )
                    )
                }
            }
        }

        val info = AnilistInfoResult(
            id = id,
            title = MangaTitle(english = media.title?.english, romaji = media.title?.romaji),
            cover = media.cover.large,
            rating = media.averageScore,
            tags = media.tags?.map { item -> item.name },
            type = media.type!!,
            startDate = media.startDate,
            endDate = media.endDate,
            description = media.description,
            banner = media.banner,
            genres = media.genres,
            source = media.source,
            status = media.status,
            chapters = media.chapters,
            synonyms = media.synonyms,
            characters = characters.toList(),
            recommenations = recommendations.toList(),
            relations = relations.toList(),
        )

        return info;
    }
}