package app.ice.readmanga.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.ice.readmanga.UserPreferences
import app.ice.readmanga.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Settings(rootNavigator: NavHostController, viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val cosco = rememberCoroutineScope()
    Scaffold { innerPad ->
        Column(Modifier.padding(top = innerPad.calculateTopPadding())) {
            Text(
                "Settings",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp, 32.dp)
            )
            Box(
                modifier = Modifier
                    .clickable {
                        cosco.launch {
                            viewModel.saveSettings(
                                viewModel.settings.value.toBuilder().setMaterialTheme(!viewModel.settings.value.materialTheme)
                                    .build()
                            )
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Material Theme",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        viewModel.settings.collectAsState().value.materialTheme,
                        onCheckedChange = { value ->
                            cosco.launch {
                                viewModel.saveSettings(
                                    viewModel.settings.value.toBuilder().setMaterialTheme(value)
                                        .build()
                                )
                            }
                        })
                }
            }
        }
    }
}