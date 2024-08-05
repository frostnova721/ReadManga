package app.ice.readmanga.ui.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.database.anilist.Anilist
import app.ice.readmanga.core.local.MangaProgress
import app.ice.readmanga.types.AnilistTrendingResult
import app.ice.readmanga.types.MangaProgressList
import app.ice.readmanga.types.MangaTitle
import app.ice.readmanga.ui.theme.Rubik
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(rootController: NavHostController, barController: NavHostController) {
    var recentsLoaded by rememberSaveable { mutableStateOf(false) }
    var trendingLoaded by rememberSaveable { mutableStateOf(false) }

    var progressList by rememberSaveable {
        mutableStateOf<List<MangaProgressList>>(emptyList())
    }

    var trendingList by rememberSaveable {
        mutableStateOf<List<AnilistTrendingResult>>(emptyList())
    }

    val cosco = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { trendingList.size })

    LaunchedEffect(Unit) {
        if (!recentsLoaded) {
            recentsLoaded = true
            cosco.launch {
                progressList = MangaProgress().getProgress(context).first()
            }
        }
        if (!trendingLoaded) {
            trendingLoaded = true
            cosco.launch {
                trendingList = Anilist().getTrending()
            }
        }
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .height(340.dp)
                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surfaceDim)
            ) {
                HorizontalPager(state = pagerState) { page ->
                    val offset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                    Box(modifier = Modifier.clickable {
                        val id = trendingList[page].id
                        rootController.navigate("info/$id")
                    }) {
                        AsyncImage(
                            model = trendingList[page].banner ?: trendingList[page].cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alpha = 0.6f,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(8.dp)
                                .graphicsLayer() {
                                    translationX = offset * size.width * 0.7f
                                }
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 80.dp, start = 30.dp, end = 30.dp)
                        ) {
                            AsyncImage(
                                model = trendingList[page].cover,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(180.dp)
                                    .width(115.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                            )
                            Column(modifier = Modifier.padding(start = 17.dp, top = 15.dp)) {
                                Text(
                                    trendingList[page].title.english
                                        ?: trendingList[page].title.romaji ?: "no_title",
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    trendingList[page].genres?.joinToString(", ") ?: "",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                ) {
                                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(end = 5.dp).height(20.dp))
                                        Text(
                                            "${(trendingList[page].rating ?: 0) / 10}/10",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 10.dp,
                            bottom = 20.dp,
                            start = 16.dp
                        )
                ) {
                    Text("ReadManga", fontSize = 26.sp, fontFamily = Rubik)
                }
            }
            HeadTitle(string = "Continue Reading")
            Box {
                if (progressList.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        Text("Empty as a blank page!")
                        Button(onClick = {
                            barController.navigate("search") {
                                popUpTo(barController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }) {
                            Text("Explore")
                        }
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ) {
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
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxHeight()
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
                                                Text("${it.read ?: "??"} / ${it.total ?: "??"}", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 5.dp, start = 5.dp))
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
