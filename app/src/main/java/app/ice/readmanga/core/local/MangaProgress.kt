package app.ice.readmanga.core.local

import android.content.Context
import android.util.Log
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

    fun getProgress(context: Context): Flow<List<MangaProgressList>> {
        return context.mangaProgressDataStore.data.map { perf ->
            val string = perf[progressKey] ?: ""
            if(string.isNotEmpty() && string != "[]") {
                json.decodeFromString<List<MangaProgressList>>(string)
            } else {
                emptyList()
            }
        }
    }

    private suspend fun saveProgress(context: Context, mangaList: List<MangaProgressList>) {
        Log.i("SAVE", "Saving progress...")
        val jsonString = json.encodeToString(mangaList)
        context.mangaProgressDataStore.edit{ perf ->
            perf[progressKey] = jsonString
        }
    }

    suspend fun updateProgressWithId(context: Context, id: Int, progress: Float) {
        val currentList = getProgress(context).first()
        val updatedItem = currentList.first { it.id == id }
        val filteredList = if(currentList.size > 40) currentList.filterNot { it.id == id }.subList(0,40) else currentList.filterNot { it.id == id }
        saveProgress(context, filteredList + updatedItem)
    }

    suspend fun updateProgress(context: Context, manga: MangaProgressList) {
        val currentList = getProgress(context).first()
        //take the list and trim it to 40
        val filtered = if(currentList.size > 40) currentList.filterNot { it.id == manga.id }.subList(0, 40) else currentList.filterNot { it.id == manga.id }
        val updated = filtered + manga
        saveProgress(context, updated.reversed())
    }
}