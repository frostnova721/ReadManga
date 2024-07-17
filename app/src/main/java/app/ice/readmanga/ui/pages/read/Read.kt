package app.ice.readmanga.ui.pages.read

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.core.source_handler.SourceHandler
import coil.compose.SubcomposeAsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Read(rootNavController: NavHostController, chapterId: String, chapterNumber: String) {

    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.findActivity()?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }

    var showUi by rememberSaveable {
        mutableStateOf(true)
    }

    var pages by rememberSaveable {
        mutableStateOf<List<String?>>(listOf(null))
    }

    val interactionSource = remember { MutableInteractionSource() }
    val scale = remember {
        mutableStateOf(1f)
    }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var columnWidth by remember { mutableStateOf(0f) }
    var columnHeight by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        if (pages[0] == null) {
            val res = SourceHandler(MangaSources.MANGA_READER).getPages(chapterId)
            if (res == null) {
                println("null")
                val toast = Toast.makeText(
                    context,
                    "Having some problems with the images! try another source",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                pages = res
            }
        }
    }

    Scaffold(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = interactionSource
        ) { showUi = !showUi }) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
                .pointerInput(Unit) {
                    detectTransformGestures() { centroid, pan, zoom, rotation ->
                        scale.value *= zoom
                        val maxX = (columnWidth * (scale.value - 1)) / 2
                        val minX = -maxX
                        offsetX = (offsetX + pan.x).coerceIn(minX, maxX)

                        val maxY = (columnHeight) / 2
                        val minY = -maxY
                        offsetY = (offsetY + pan.y).coerceIn(minY, maxY)
                    }
                }
                .onGloballyPositioned { layoutCoordinates ->
                    columnWidth = layoutCoordinates.size.width.toFloat()
                    columnHeight = layoutCoordinates.size.height.toFloat()
                }
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(3f, scale.value)),
                    scaleY = maxOf(1f, minOf(3f, scale.value)),
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            items(pages.size) { item ->
                if (pages[item] == null) CircularProgressIndicator()
                else {
                    SubcomposeAsyncImage(
                        model = pages[item],
                        contentDescription = "page_$item",
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .height(700.dp)
                                    .fillMaxWidth(), contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }, modifier = Modifier.fillMaxWidth()
//                            .graphicsLayer(
                        // adding some zoom limits (min 50%, max 200%)
//                            scaleX = maxOf(1f, minOf(3f, scale.value)),
//                            scaleY = maxOf(1f, minOf(3f, scale.value)),
//                        ),
                    )
                }
            }
        }

        //top bar
        if (showUi)
            AnimatedVisibility(
                visible = showUi,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Black.copy(alpha = 0.8F),
                                    Color.Black.copy(alpha = 0.7F),
                                    Color.Black.copy(alpha = 0.6F),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(top = 60.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        IconButton(onClick = { rootNavController.navigateUp() }) {
                            Icon(
                                FeatherIcons.ArrowLeft,
                                contentDescription = "back",
                                tint = Color.White
                            )
                        }
                        Text(
                            "Chapter: $chapterNumber",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
    }
}