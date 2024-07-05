package app.ice.readmanga.utils

import com.google.gson.Gson
import com.google.gson.JsonPrimitive
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import org.json.JSONObject

suspend fun get(url: String): String {
    val client = HttpClient()
    val response = client.get(url)
    return response.bodyAsText()
}

suspend fun gqlRequest(url: String, query: String): String {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    val reqBody = JSONObject().put("query", query)

    val response = client.post(url){
      setBody(reqBody.toString())
        contentType(ContentType.Application.Json)
    }.bodyAsText()

    client.close()

    return response;
}