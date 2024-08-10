package app.ice.readmanga.ui.pages

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
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
import app.ice.readmanga.ui.navigator.Routes
import app.ice.readmanga.ui.theme.Rubik
import coil.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.RefreshCw
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Home(rootController: NavHostController, barController: NavHostController) {
    var recentsLoaded by remember { mutableStateOf(false) }
    var trendingLoaded by rememberSaveable { mutableStateOf(false) }
    var trendingFetchFailed by rememberSaveable {
        mutableStateOf(false)
    }

    val progressList = remember {
        mutableStateListOf<MangaProgressList>()
    }

    var trendingList by rememberSaveable {
        mutableStateOf<List<AnilistTrendingResult>>(emptyList())
    }

    val cosco = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { trendingList.size })

    LaunchedEffect(key1 = pagerState.settledPage) {
        launch {
            delay(5000)
            with(pagerState) {
                val target = if (currentPage < trendingList.size - 1) currentPage + 1 else 0
                pagerState.animateScrollToPage(
                    page = target, animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        println("RECOMpoSIng!!")
        if (!recentsLoaded) {
            recentsLoaded = true
            cosco.launch {
                progressList.addAll(MangaProgress().getProgress(context).first())
            }
        }
        if (!trendingLoaded) {
            trendingLoaded = true
            cosco.launch {
                trendingList = Anilist().getTrending()
                if(trendingList.isEmpty()) {
                    trendingFetchFailed = true
                }
            }
        }
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surfaceDim)
            ) {
                if(trendingFetchFailed && trendingList.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(
                        top = WindowInsets.statusBars.asPaddingValues()
                            .calculateTopPadding() + 85.dp,
                        start = 30.dp,
                        end = 30.dp
                    )) {
                        Text("Failed to fetch the trending!")
                        IconButton(onClick = {
                            trendingFetchFailed = false
                            cosco.launch {
                                trendingList = Anilist().getTrending()
                                if(trendingList.isEmpty()) {
                                    trendingFetchFailed = true
                                }
                            }
                        }) {
                          Icon(FeatherIcons.RefreshCw, contentDescription = null)
                        }
                    }
                }
                else if (trendingLoaded && trendingList.isNotEmpty()) {
                    HorizontalPager(state = pagerState) { page ->
                        val offset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        val item = trendingList[page]

                        Box(modifier = Modifier.clickable {
                            val id = item.id
                            rootController.navigate(Routes.InfoRoute(id = id))
                        }) {
                            AsyncImage(
                                model = item.banner ?: item.cover,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                alpha = 0.6f,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(8.dp)
                                    .graphicsLayer() {
                                        translationX = offset * size.width * 0.65f
                                    }
                            )
                            Row(
                                modifier = Modifier
                                    .padding(
                                        top = WindowInsets.statusBars.asPaddingValues()
                                            .calculateTopPadding() + 85.dp,
                                        start = 30.dp,
                                        end = 30.dp
                                    )
                            ) {
                                AsyncImage(
                                    model = item.cover,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(160.dp)
                                        .width(115.dp)
                                        .clip(
                                            RoundedCornerShape(10.dp)
                                        )
                                )
                                Column(modifier = Modifier.padding(start = 17.dp, top = 15.dp)) {
                                    Text(
                                        item.title.english
                                            ?: item.title.romaji ?: "no_title",
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        item.genres?.joinToString(", ") ?: "",
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
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier
                                                    .padding(end = 5.dp)
                                                    .height(20.dp)
                                            )
                                            Text(
                                                "${(item.rating ?: 0) / 10}/10",
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
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = WindowInsets.statusBars
                                .asPaddingValues()
                                .calculateTopPadding() + 10.dp,
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
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .padding(top = 15.dp)
                    ) {
                        items(progressList.size) { ind ->
                            val it = progressList[ind]
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(115.dp)
                                    .width(260.dp)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .clickable {
                                        rootController.navigate(Routes.InfoRoute(id = it.id))
                                    }
                            ) {
                                Row {
                                    AsyncImage(
                                        model = it.cover,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(5.dp)
                                            .clip(
                                                RoundedCornerShape(10.dp - 5.dp)
                                            )
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
                                                start = 10.dp
                                            )
                                        )
                                        Text(
                                            "${it.read ?: "??"} / ${it.total ?: "??"}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                top = 8.dp,
                                                start = 10.dp
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

@Composable
fun HeadTitle(string: String) {
    Text(
        string,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
    )
}
