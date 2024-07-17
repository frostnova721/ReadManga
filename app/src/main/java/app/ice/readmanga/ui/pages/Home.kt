package app.ice.readmanga.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.ice.readmanga.core.providers.MangaDex
import kotlinx.coroutines.launch

@Composable
fun Home(rootController: NavHostController, barController: NavHostController) {

    val coroutineScope = rememberCoroutineScope()
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    val cosco = rememberCoroutineScope()

    Box(Modifier.safeDrawingPadding()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "To be implemented")

            Button(onClick = {
                cosco.launch { MangaDex().search("") }

            }) {
                Text("perfrom shit!")
            }
        }
    }
}
