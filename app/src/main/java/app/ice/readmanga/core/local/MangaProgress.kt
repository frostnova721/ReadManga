package app.ice.readmanga.core.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.ice.readmanga.types.MangaProgressList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.mangaProgressDataStore by preferencesDataStore(name= "manga_progress")

class MangaProgress {
    private val progressKey = stringPreferencesKey("progressList")
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getProgress(context: Context): Flow<List<MangaProgressList>> {
        return context.mangaProgressDataStore.data.map { perf ->
            val string = perf[progressKey] ?: ""
            if(string.isNotEmpty() && string != "[]") {
                json.decodeFromString<List<MangaProgressList>>(string)
            } else {
                emptyList<MangaProgressList>()
            }
        }
    }

    private suspend fun saveProgress(context: Context, mangaList: List<MangaProgressList>) {
        val jsonString = json.encodeToString(mangaList)
        context.mangaProgressDataStore.edit{ perf ->
            perf[progressKey] = jsonString
        }
    }

    suspend fun updateProgressWithTitle(context: Context, title: String, progress: Float) {
        val currentList = getProgress(context).first()
        val updatedList = currentList.map { item ->
            if(item.title == title) {
                item.copy(read = progress)
            } else {
                item
            }
        }
        saveProgress(context, updatedList)
    }

    suspend fun updateProgress(context: Context, manga: MangaProgressList) {
        val currentList = getProgress(context).first()
        val filtered = currentList.filterNot { it.id == manga.id }
        val updated = filtered + manga
        saveProgress(context, updated)
    }
}