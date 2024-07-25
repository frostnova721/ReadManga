package app.ice.readmanga.ui.pages.info

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
import compose.icons.feathericons.Calendar


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
                val mangas = SourceHandler(MangaSources.MANGADEX).search(title)
                val chaps = SourceHandler(MangaSources.MANGADEX).getChapters(mangas[0].id)
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

    Scaffold { innerPadding ->
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
                            alpha = 0.35F,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedIconButton(onClick = {
                                rootNavigator.popBackStack()
                            }) {
                                Icon(FeatherIcons.ArrowLeft, contentDescription = null)
                            }
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 30.dp, start = 30.dp)
                                        .height(180.dp)
                                        .width(125.dp)
//                                        .align(Alignment.Start)
                                        .clip(RoundedCornerShape(10))
                                ) {
                                    AsyncImage(
                                        model = info!!.cover,
                                        contentDescription = "cover",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                                Column(modifier = Modifier
                                    .padding(top = 50.dp, start = 20.dp, end = 30.dp)){
                                    Text(
                                        text = info!!.title.english ?: info!!.title.romaji ?: "",
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Left,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = info!!.type,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .padding(top = 10.dp)

                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 15.dp)) {
                                        Button(
                                            onClick = {
                                                showReadPage = !showReadPage
                                            },
                                            shape = RoundedCornerShape(22.dp),
                                            modifier = Modifier
                                                .width(115.dp)
                                                .height(55.dp)
                                        ) {
                                            Text(
                                                if (showReadPage) "Read" else "Info",
                                                fontSize = 23.sp,
                                            )
                                        }
                                        FilledIconButton(onClick = { Toast.makeText(context, "not implied yet!", Toast.LENGTH_SHORT).show() },) {
                                            Icon(FeatherIcons.Calendar, contentDescription = null, modifier = Modifier.height(20.dp))
                                        }
                                    }
                                }
                            }
//                            Box(modifier = Modifier
//                                .align(Alignment.CenterHorizontally)
//                                .padding(top = 15.dp)
//                                .clip(RoundedCornerShape(10.dp))
//                                .border(
//                                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
//                                    shape = RoundedCornerShape(10.dp)
//                                )
//                                .padding(8.dp)
//                            )
//                                 {
//                                Row(horizontalArrangement = Arrangement.Center) {
//                                    Box(modifier = Modifier
//                                        .clip(RoundedCornerShape(5.dp))
//                                        .background(if (showReadPage) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
//                                        .clickable { showReadPage = false }
//                                        .padding(
//                                            top = 5.dp,
//                                            bottom = 5.dp,
//                                            start = 10.dp,
//                                            end = 10.dp
//                                        )
//                                    ) {
//                                        Text("info", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                                    }
//                                    Box(modifier = Modifier
//                                        .clip(RoundedCornerShape(5.dp))
//                                        .background(if (!showReadPage) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
//                                        .clickable { showReadPage = true }
//                                        .padding(
//                                            top = 5.dp,
//                                            bottom = 5.dp,
//                                            start = 10.dp,
//                                            end = 10.dp
//                                        )
//                                    ) {
//                                        Text("read", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                                    }
//                                }
//                            }
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
            bottom = 10.dp,
//            start = 20.dp
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