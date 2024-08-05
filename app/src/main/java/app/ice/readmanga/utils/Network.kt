package app.ice.readmanga.utils


import android.util.Log
import app.ice.readmanga.types.anilistResponses.AnilistResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.json.JSONObject

public val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

suspend fun get(url: String): HttpResponse {
    val res = client.get(url)
    return res
}

suspend fun gqlRequest(url: String, query: String): AnilistResponse?  {
    try {
        val reqBody = JSONObject().put("query", query).toString()

        val res = client.post(url) {
            setBody(reqBody)
            contentType(ContentType.Application.Json)
        }

        return res.body<AnilistResponse?>();
    } catch (err: Exception) {
        Log.e("REQ_ERR", err.message ?: "Error while sending post req")
        return null;
    }
}