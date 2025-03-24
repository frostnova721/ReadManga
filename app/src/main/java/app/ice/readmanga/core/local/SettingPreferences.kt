package app.ice.readmanga.core.local

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import app.ice.readmanga.UserPreferences
import app.ice.readmanga.types.PreferencesSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

val Context.settingPreferenceDataStore by dataStore("user_perfs.pb", PreferencesSerializer)

class SettingPreferences (private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    private val key = stringPreferencesKey("pref")

    fun getPrefences(): Flow<UserPreferences> {
        return context.settingPreferenceDataStore.data
    }

    suspend fun savePreferences(preferences: UserPreferences) {
        Log.i("SAVE", "Saving preference...")
        context.settingPreferenceDataStore.updateData {
            preferences
        }
    }
}