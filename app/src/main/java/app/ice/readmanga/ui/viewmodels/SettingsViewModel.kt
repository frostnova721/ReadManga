package app.ice.readmanga.ui.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ice.readmanga.UserPreferences
import app.ice.readmanga.core.local.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repo: SettingPreferences) : ViewModel() {

    private val _settings = MutableStateFlow(UserPreferences.getDefaultInstance()) // ✅ Initialize properly
    val settings: StateFlow<UserPreferences> get() = _settings

    init {
        viewModelScope.launch {
            repo.getPrefences().collect { prefs ->
                _settings.value = prefs
            }
        }
    }

    suspend fun saveSettings(value: UserPreferences) {
        repo.savePreferences(value)
        _settings.value = value
    }
}
