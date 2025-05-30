package app.ice.readmanga.ui.models

import android.graphics.Color
import android.graphics.drawable.Icon
import android.widget.StackView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.ui.navigator.Routes
import coil.ImageLoader
import coil.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.Star

class Cards(private val navController: NavController) {

    @Composable
    fun mangaCard(id: Int, title: String, imageUrl: String, averageScore: Int?) {
        Box(modifier = Modifier
            .width(130.dp)
            .padding(5.dp)
            .clickable {
                navController.navigate(Routes.InfoRoute(id = id))
            }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .width(120.dp)
                            .height(170.dp),
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.87F),
                                shape = RoundedCornerShape(0.dp, 5.dp, 0.dp, 0.dp)
                            )
                            .padding(start = 5.dp, end = 5.dp, bottom = 2.dp, top = 2.dp)
                            .align(alignment = Alignment.BottomStart)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                FeatherIcons.Star,
                                contentDescription = "star",
                                modifier = Modifier.width(15.dp),
                                tint = androidx.compose.ui.graphics.Color.Black
                            )
                            Text(
                                "${if (averageScore != null) (averageScore / 10.0) else "??"}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                }
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }

    @Composable
    fun VerticalMangaCard(id: Int, title: String, cover: String, readProgress: Float?, totalChapters: Int? ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .height(115.dp)
                .width(260.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f))
//                .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate(Routes.InfoRoute(id = id))
                }
        ) {
            Row {
                AsyncImage(
                    model = cover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp)
                        .padding(5.dp)
                        .clip(
                            RoundedCornerShape(10.dp - 5.dp)
                        )
                )
                Column {
                    Text(
                        title,
                        maxLines = 2,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 10.dp,
                            start = 10.dp,
                            end = 10.dp
                        )
                    )
                    Text(
                        "${readProgress ?: "??"} / ${totalChapters ?: "??"}",
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

@Preview
@Composable
fun verticalCardPreview() {
    val navController = rememberNavController()
    Cards(navController).VerticalMangaCard(
        id = 6942,
        title = "Manga Title",
        cover = "https://s4.anilist.co/file/anilistcdn/media/manga/cover/medium/nx86123-yRZuDFrUEDGu.png",
        readProgress = 5f,
        totalChapters = 69
    )
}