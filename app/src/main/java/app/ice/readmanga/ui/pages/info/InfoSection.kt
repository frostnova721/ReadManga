package app.ice.readmanga.ui.pages.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ice.readmanga.types.AnilistInfoResult
import java.util.Locale

@Composable
fun InfoSection(info: AnilistInfoResult) {
   var seeMoreSynopsis by remember {
      mutableStateOf(false)
   }

   Column(modifier = Modifier.padding(top = 40.dp, start = 30.dp, end = 30.dp)) {
      ItemTitle(title = "Genres")
      Text(info.genres!!.joinToString(" • "), fontSize = 16.sp, color = Color.Gray,
         )

      ItemTitle(title = "Synopsis")
      Box(modifier = Modifier
         .clip(RoundedCornerShape(20.dp))
         .clickable { seeMoreSynopsis = !seeMoreSynopsis }
         .background(MaterialTheme.colorScheme.surfaceDim)
         .padding(10.dp)
      ) {
         Text(text = info.description ?: "no description found",
            maxLines = if(seeMoreSynopsis) Int.MAX_VALUE else 10,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            )
      }
      ItemTitle(title = "More Info")
      moreInfoItem(text = "Status: " +( info.status ?: ""))
      moreInfoItem(text = "Source: " + (info.source ?: "unknown"))
   }
}

@Composable
fun moreInfoItem(text: String) {
   Text(text = text.replace("_", "").lowercase(), fontWeight = FontWeight.Bold, fontSize = 15.sp)
}