package app.ice.readmanga.ui.models

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import coil.ImageLoader
import coil.compose.AsyncImage

class Cards {

    @Composable
    fun mangaCard(title: String, imageUrl: String) {
        Column {
            AsyncImage(model = imageUrl, contentDescription = "")
        }
    }
}