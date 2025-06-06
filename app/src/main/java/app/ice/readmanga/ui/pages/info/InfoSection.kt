package app.ice.readmanga.ui.pages.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ice.readmanga.types.AnilistInfoResult
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun InfoSection(info: AnilistInfoResult) {
   var seeMoreSynopsis by remember {
      mutableStateOf(false)
   }

   Column(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
      ItemTitle(title = "Genres")
      Text(info.genres!!.joinToString(" • "), fontSize = 16.sp, color = Color.Gray,
         modifier = Modifier.padding(bottom = 10.dp) )

      ItemTitle(title = "Titles")
      Text("• ${info.title.english}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 15.dp))
      Text("• ${info.title.romaji}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp, start = 15.dp))

      ItemTitle(title = "Synopsis")
      Box(modifier = Modifier
         .clip(RoundedCornerShape(20.dp))
         .clickable { seeMoreSynopsis = !seeMoreSynopsis }
         .background(MaterialTheme.colorScheme.surfaceDim)
         .padding(10.dp)
      ) {
         Text(text = info.description?.replace(Regex("<[^>]*>"), "") ?: "no description found",
            maxLines = if(seeMoreSynopsis) Int.MAX_VALUE else 10,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            )
      }
      ItemTitle(title = "More Info")
      moreInfoItem(text = "Status: " +( info.status ?: "??"))
      moreInfoItem(text = "Source: " + (info.source ?: "??"))
      moreInfoItem(text = "Rating: " + (if(info.rating == null) "??" else info.rating / 10) + "/10")

      //just a spacer
      Box(modifier = Modifier.height(10.dp))

      ItemTitle(title = "Characters")
      Box(modifier = Modifier.padding(top = 10.dp).height(230.dp).fillMaxWidth()) {
         if (info.characters != null) {
            LazyRow {
               items(info.characters.size!!) { ind ->
                  val it = info.characters[ind]
                  Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     modifier = Modifier.width(120.dp).padding(end = 15.dp)
                  ) {
                     AsyncImage(
                        model = it.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(170.dp).width(115.dp)
                     )
                     Text(
                        it.name.full ?: "???",
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3
                     )
                  }
               }
            }
         } else {
            Text("No Character Data!", fontSize = 17.sp, fontWeight = FontWeight.Bold ,textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.Center))
         }
      }
   }
}

@Composable
fun moreInfoItem(text: String) {
   Text(text = text.replace("_", ""), fontWeight = FontWeight.Bold, fontSize = 15.sp)
}