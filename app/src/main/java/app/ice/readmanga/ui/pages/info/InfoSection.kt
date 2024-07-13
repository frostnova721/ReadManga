package app.ice.readmanga.ui.pages.info

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.ice.readmanga.types.AnilistInfoResult

@Composable
fun InfoSection(info: AnilistInfoResult) {
    Box(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(
                top = 10.dp,
                bottom = 10.dp,
                start = 15.dp,
                end = 15.dp
            )
    ) {
        Column {
            InfoItem(key = "source", label = info.source)
            InfoItem(
                key = "chapters",
                label = info.chapters?.toString() ?: "??"
            )
            InfoItem(key = "status", label = info.status)
            InfoItem(
                key = "rating",
                label = ((info.rating ?: 0) / 10.0).toString()
            )

        }
    }
    ItemTitle(title = "Genres")
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        info.genres!!.forEach { genre ->
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(15))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(text = genre, modifier = Modifier.padding(10.dp))
            }
        }
    }
    ItemTitle(title = "Tags")
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        info.tags!!.forEach { genre ->
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(15))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(text = genre, modifier = Modifier.padding(10.dp))
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 40.dp)
            .clip(RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column {
            ItemTitle(title = "Description")
            Text(
                info.description?.replace(Regex("""<[^>]*>"""), "") ?: "",
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
            )
        }
    }
}