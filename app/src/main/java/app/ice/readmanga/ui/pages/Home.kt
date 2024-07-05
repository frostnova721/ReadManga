package app.ice.readmanga.ui.pages

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.Downloader
import app.ice.readmanga.core.providers.MangaReader
import app.ice.readmanga.types.ChaptersResult
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun Home(navHostController: NavHostController) {

    val coroutineScope = rememberCoroutineScope()
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    Box(Modifier.safeDrawingPadding()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nigger nigger")
        }
    }
}
