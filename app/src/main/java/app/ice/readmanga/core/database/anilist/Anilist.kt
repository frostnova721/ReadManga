package app.ice.readmanga.core.database.anilist

import app.ice.readmanga.utils.get
import app.ice.readmanga.utils.gqlRequest

class Anilist {

    private val url = "https://graphql.anilist.co/"

    suspend fun search(query: String) {
        val query = AnilistQueries().searchQuery(query)
        val res = gqlRequest(url, query)

        //map this shit!
    }
}