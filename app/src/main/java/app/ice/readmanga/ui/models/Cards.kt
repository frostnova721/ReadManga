package app.ice.readmanga.ui.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage

class Cards {

    @Composable
    fun mangaCard(title: String, imageUrl: String) {
        Box(modifier = Modifier.width(110.dp).padding(5.dp)) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(model = imageUrl, contentDescription = "", contentScale = ContentScale.Crop,
                    modifier = Modifier.width(110.dp)
                        .height(160.dp),
                    )
                Text(text = title, maxLines = 2 , overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold , modifier = Modifier.padding(top= 10.dp))
            }
        }
    }
}