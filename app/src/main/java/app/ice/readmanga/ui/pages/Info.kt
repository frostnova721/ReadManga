package app.ice.readmanga.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import app.ice.readmanga.core.database.anilist.Anilist
import app.ice.readmanga.types.AnilistInfoResult
import coil.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.launch


@Composable
fun Info(id: Int, rootNavigator: NavHostController) {

    var info by rememberSaveable { mutableStateOf<AnilistInfoResult?>(null) }

    LaunchedEffect(Unit) {
        if (info == null) {
            val res = Anilist().getInfo(id = id)
            info = res
            println("done!")
        }
    }

    Scaffold { innerPadding ->
        if (info != null) {
            println("not null!")
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStartPercent = 10, bottomEndPercent = 10))
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
                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
                            .clip(RoundedCornerShape(15))
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(info!!.source?: "??")
                            Text("")
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