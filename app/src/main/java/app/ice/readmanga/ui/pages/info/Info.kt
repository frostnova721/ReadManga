package app.ice.readmanga.ui.pages.info

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.Navigation.findNavController
import app.ice.readmanga.core.database.anilist.Anilist
import app.ice.readmanga.core.local.MangaProgress
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.core.source_handler.SourceHandler
import app.ice.readmanga.types.AnilistInfoResult
import app.ice.readmanga.types.Chapters
import app.ice.readmanga.utils.showToast
import coil.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Heart
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

private suspend fun getChaptersReadForThisMangaOrManhuaOrWhatever(
    id: Int,
    context: Context
): Float? {
    val mangas = MangaProgress().getProgress(context).first()
    val filteredOutItem = mangas.firstOrNull() { it.id == id }
    return filteredOutItem?.read
}

@Composable
fun Info(id: Int, rootNavigator: NavHostController, infoSharedViewModel: InfoSharedViewModel) {

    val context = LocalContext.current

    var info by rememberSaveable { mutableStateOf<AnilistInfoResult?>(null) }

    var showReadPage by rememberSaveable { mutableStateOf(false) }

    var chapters by rememberSaveable {
        mutableStateOf<List<Chapters?>>(listOf(null))
    }

    var favourited by rememberSaveable {
        mutableStateOf(false)
    }

    val source by rememberSaveable { mutableStateOf(infoSharedViewModel.source.value) }

    LaunchedEffect(Unit) {
        if (info == null) {
            try {
                val res = Anilist().getInfo(id = id)
                info = res
                infoSharedViewModel.title = res?.title?.english ?: res?.title?.romaji
                infoSharedViewModel.coverImage = res?.cover
                infoSharedViewModel.id = id

                val readChapters = getChaptersReadForThisMangaOrManhuaOrWhatever(id, context)
                infoSharedViewModel.updateReadChapters(readChapters ?: 0f)

                println("done loading infos!")
            } catch (err: Exception) {
                Log.e("INFO ERR", err.toString())
                showToast(context, "Couldn't load the info page!")
                rootNavigator.navigateUp()
            }
        }
        if (chapters[0] == null && info != null) {
            try {
                val title = info!!.title.english ?: info!!.title.romaji ?: ""
                val mangas = SourceHandler(source).search(title)
                infoSharedViewModel.updateFoundTitle(mangas[0].title)
                val chaps = SourceHandler(source).getChapters(mangas[0].id)
                if (chaps.isNotEmpty()) {
                    val eng = chaps.filter { item -> item.lang == "en" }
                    chapters = emptyList()
                    chapters = eng[0].chapters.reversed()
                    infoSharedViewModel.addChapters(chapters)
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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        bottom = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {
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
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedIconButton(onClick = {
                                    infoSharedViewModel.clearViewModel()
                                    rootNavigator.navigateUp()
                                }) {
                                    Icon(FeatherIcons.ArrowLeft, contentDescription = null)
                                }
                                Icon(
                                    if (favourited) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .padding(end = 15.dp)
                                        .size(30.dp, 30.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = {
                                                    favourited = !favourited
                                                    showToast(context, "does nothing btw")
                                                }
                                            )
                                        })
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
                                Column(
                                    modifier = Modifier
                                        .padding(top = 50.dp, start = 20.dp, end = 30.dp)
                                ) {
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
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(top = 15.dp)
                                    ) {
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
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        FilledIconButton(
                                            onClick = {
                                                Toast.makeText(
                                                    context,
                                                    "not implied yet!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                        ) {
                                            Icon(
                                                FeatherIcons.Calendar,
                                                contentDescription = null,
                                                modifier = Modifier.height(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (showReadPage) ReadSection(
                    infoSharedViewModel,
                    rootNavigator
                ) else InfoSection(info = info!!)
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
fun ItemTitle(title: String, startPadding: Int = 0) {
    Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            top = 20.dp,
            bottom = 10.dp,
            start = startPadding.dp
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