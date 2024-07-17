package app.ice.readmanga.core.providers

import app.ice.readmanga.types.MangadexItem
import app.ice.readmanga.types.MangadexResponse
import app.ice.readmanga.utils.get
import io.ktor.client.call.body

class MangaDex {
    suspend fun search(query: String) {
//        val res = get("https://api.mangadex.org/manga?title=oreshura").body<MangadexResponse>()
//        val data: List<MangadexItem> = res.data
    }
}