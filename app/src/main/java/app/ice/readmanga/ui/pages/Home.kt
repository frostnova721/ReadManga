package app.ice.readmanga.ui.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.local.MangaProgress
import app.ice.readmanga.core.providers.MangaDex
import app.ice.readmanga.types.MangaProgressList
import app.ice.readmanga.ui.theme.Rubik
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(rootController: NavHostController, barController: NavHostController) {
    val recentsLoaded = rememberSaveable { mutableStateOf(false) }

    var progressList by rememberSaveable {
        mutableStateOf<List<MangaProgressList>>(emptyList())
    }

    val cosco = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 10 })

    LaunchedEffect(Unit) {
        if (!recentsLoaded.value) {
            cosco.launch {
                progressList = MangaProgress().getProgress(context).first()
                println(progressList)
            }
        }
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box {
                HorizontalPager(state = pagerState) { page ->
                    Box(
                        modifier = Modifier
                            .height(340.dp)
                            .fillMaxWidth()
                            .background(Color.Cyan)
                    ) {
                        Text("HIII")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp, start = 16.dp)
                ) {
                    Text("ReadManga", fontSize = 26.sp, fontFamily = Rubik)
                }
            }
            HeadTitle(string = "Continue Reading")
            Box {
                LazyRow(modifier = Modifier.fillMaxWidth().padding(top = 15.dp)) {
                    items(progressList.chunked(2)) { pair ->
                        Column(modifier = Modifier.padding(end = 10.dp)) {
                            pair.forEach {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(115.dp)
                                        .width(240.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .clickable {
                                            rootController.navigate("info/${it.id}")
                                        }
                                ) {
                                    Row {
                                        AsyncImage(
                                            model = it.cover,
                                            contentDescription = null
                                        )
                                        Column {
                                            Text(
                                                it.title,
                                                maxLines = 2,
                                                fontSize = 16.sp,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    start = 5.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeadTitle(string: String) {
    Text(
        string,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
    )
}
