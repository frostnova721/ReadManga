package app.ice.readmanga.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

suspend fun get(url: String): String {
    val client = HttpClient()
    val response = client.get(url)
    return response.bodyAsText()
}