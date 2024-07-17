package app.ice.readmanga.ui.pages.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.database.anilist.Anilist
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.core.source_handler.SourceHandler
import app.ice.readmanga.types.AnilistInfoResult
import app.ice.readmanga.types.Chapters
import coil.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft


@Composable
fun Info(id: Int, rootNavigator: NavHostController, infoSharedViewModel: InfoSharedViewModel) {

    val context = LocalContext.current

    var info by rememberSaveable { mutableStateOf<AnilistInfoResult?>(null) }

    var showReadPage by rememberSaveable { mutableStateOf(false) }

    var chapters by rememberSaveable {
        mutableStateOf<List<Chapters?>>(listOf(null))
    }

    LaunchedEffect(Unit) {
        if (info == null) {
            val res = Anilist().getInfo(id = id)
            info = res
            infoSharedViewModel.title = res?.title?.english ?: res?.title?.romaji
            println("done!")
        }
        if (chapters[0] == null && info != null) {
            try {
                val title = info!!.title.english ?: info!!.title.romaji ?: ""
                val mangas = SourceHandler(MangaSources.MANGA_READER).search(title)
                val chaps = SourceHandler(MangaSources.MANGA_READER).getChapters(mangas[0].id)
                if (chaps.isNotEmpty()) {
                    val eng = chaps.filter { item -> item.lang == "en" }
                    chapters = emptyList()
                    chapters = eng[0].chapters.reversed()
                } else {
                    chapters = emptyList()
                }
            } catch (e: Exception) {
                chapters = emptyList()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if(info != null)
            ExtendedFloatingActionButton(onClick = {
                showReadPage = !showReadPage
            }) {
                Box(modifier = Modifier.width(50.dp)) {
                    Text(
                        if (showReadPage) "Info" else "Read",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { innerPadding ->
        if (info != null) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomStartPercent = 10,
                                    bottomEndPercent = 10
                                )
                            )
                            .fillMaxWidth()
                            .height(330.dp)
                    ) {
                        AsyncImage(
                            model = info!!.banner ?: info!!.cover,
                            contentDescription = "banner",
                            contentScale = ContentScale.Crop,
                            alpha = 0.4F,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                rootNavigator.popBackStack()
                            }) {
                                Icon(FeatherIcons.ArrowLeft, contentDescription = null)
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .height(190.dp)
                                    .width(130.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clip(RoundedCornerShape(10))
                            ) {
                                AsyncImage(
                                    model = info!!.cover,
                                    contentDescription = "cover",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Text(
                                text = info!!.title.english ?: info!!.title.romaji ?: "",
                                fontSize = 23.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterHorizontally)
                                    .padding(top = 50.dp, start = 50.dp, end = 50.dp)
                            )
                            Text(
                                text = info!!.type,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 15.dp)

                            )
                            if(showReadPage) ReadSection(chapters, infoSharedViewModel, rootNavigator) else InfoSection(info = info!!)
                        }
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ItemTitle(title: String) {
    Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            top = 20.dp,
            bottom = 15.dp,
            start = 20.dp
        )
    )
}

@Composable
fun InfoItem(key: String, label: String?) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(key, fontWeight = FontWeight.Bold)
        Text(label?.lowercase() ?: "??")
    }
}