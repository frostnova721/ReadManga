package app.ice.readmanga.ui.pages.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.providers.MangaReader
import app.ice.readmanga.types.Chapters
import app.ice.readmanga.types.ChaptersResult
import java.net.URLEncoder

@Composable
fun ReadSection(chapters: List<Chapters?>, rootNavHostController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier.padding(top = 20.dp),
                border = BorderStroke(color = MaterialTheme.colorScheme.primary, width = 1.dp)
            ) {
                Text(text = "source", fontSize = 20.sp)
            }
            Text(
                text = "Chapters",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 18.dp, bottom = 20.dp)
            )
            Row {

            }
            if (chapters[0] == null) CircularProgressIndicator()
            else
                LazyVerticalGrid(columns = GridCells.Adaptive(75.dp), modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                    items(chapters.size) { ind ->
                        chapterItem(chapter = chapters[ind]!!, rootNavHostController)
                    }
                }
        }
    }
}

@Composable
fun chapterItem(chapter: Chapters, rootNavHostController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .height(70.dp).width(70.dp)
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(20.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(chapter.chapter, modifier = Modifier.clickable {
            val encodedUrl = URLEncoder.encode(chapter.link, "UTF-8")
            rootNavHostController.navigate("read/${encodedUrl}")
        })
    }
}