package app.ice.readmanga.types

import android.util.Log
import kotlinx.serialization.Serializable
import androidx.datastore.core.Serializer
import app.ice.readmanga.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

//@Serializable
//data class UserPreferences(
//    val materialTheme: Boolean = false,
//);

object PreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences {
        return try {
            withContext(Dispatchers.IO) {
               UserPreferences.parseFrom(input)
            }
        } catch (err: Exception) {
            Log.e("PROTO-READ", "Couldnt read proto!")
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        return withContext(Dispatchers.IO) {
                t.writeTo(output)
            }
    }

}