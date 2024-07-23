package app.ice.readmanga.ui.pages.info

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.downloader.Downloader
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.core.source_handler.SourceHandler
import app.ice.readmanga.types.Chapters
import compose.icons.FeatherIcons
import compose.icons.feathericons.BookOpen
import compose.icons.feathericons.Download
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadSection(chapters: List<Chapters?>, infoSharedViewModel: InfoSharedViewModel, rootNavHostController: NavHostController) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }


    fun changeSheetState(state: Boolean) {
        showSheet = state
    }

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
            if (chapters.isNotEmpty() && chapters[0] == null) CircularProgressIndicator()
            else if(chapters.isEmpty()) Text("Couldn't get any chapters")
            else
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(75.dp),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                ) {
                    items(chapters.size) { ind ->
                        ChapterItem(
                            chapter = chapters[ind]!!,
                            rootNavHostController,
                            infoSharedViewModel,
                            changeSheetState = { sheetState -> changeSheetState(sheetState)}
                        )
                    }
                }
        }
    }

    if (showSheet)
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                infoSharedViewModel.selectedChapterLink = ""
                infoSharedViewModel.selectedChapterNumber = ""
            },
            sheetState = sheetState,
            windowInsets = WindowInsets(0,0,0,0)
        ) {
            BottomSheetContent(
                infoSharedViewModel = infoSharedViewModel,
                rootNavigator = rootNavHostController,
                changeSheetState = { sheetState -> changeSheetState(sheetState)}
            )
        }

}

@Composable
fun BottomSheetContent(infoSharedViewModel: InfoSharedViewModel, rootNavigator: NavHostController, changeSheetState: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())) {
        Text("Chapter: ${infoSharedViewModel.selectedChapterNumber}", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.CenterHorizontally))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = {
                    val encodedUrl =
                        URLEncoder.encode(infoSharedViewModel.selectedChapterLink!!, "UTF-8")
                    rootNavigator.navigate("read/$encodedUrl/${infoSharedViewModel.selectedChapterNumber}")
                }, modifier = Modifier
                    .padding(end = 10.dp)
                    .width(150.dp)
                    .height(100.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(FeatherIcons.BookOpen, contentDescription = "read")
                    Text(
                        "Read",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        val urls = SourceHandler(MangaSources.MANGADEX).getPages(
                            infoSharedViewModel.selectedChapterLink ?: error("no link my guy!!!")
                        )
                        if (urls == null) Toast.makeText(
                            context,
                            "Had some errors with pages!",
                            Toast.LENGTH_SHORT
                        ).show()
                        else {
                            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
                            val title = infoSharedViewModel.title ?: "manga"
                            Downloader().startDownload(urls, "$title-${infoSharedViewModel.selectedChapterNumber}", context)
                            changeSheetState(false)
                        }
                    }
                }, modifier = Modifier
                    .width(150.dp)
                    .height(100.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(FeatherIcons.Download, contentDescription = "download")
                    Text(
                        "Download",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ChapterItem(
    chapter: Chapters,
    rootNavHostController: NavHostController,
    sharedViewModel: InfoSharedViewModel,
    changeSheetState: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .height(70.dp)
            .width(70.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        val encodedUrl = URLEncoder.encode(chapter.link, "UTF-8")
                        rootNavHostController.navigate("read/${encodedUrl}/${chapter.chapter}")
                    },
                    onLongPress = {
                        sharedViewModel.selectedChapterLink = chapter.link
                        sharedViewModel.selectedChapterNumber = chapter.chapter
//                        selectItem(chapter.link, chapter.chapter)
                        changeSheetState(true)
                    })
            }
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            chapter.chapter,
        )
    }
}
