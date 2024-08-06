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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.R
import app.ice.readmanga.core.local.MangaProgress
import app.ice.readmanga.core.source_handler.MangaSources
import app.ice.readmanga.core.source_handler.SourceHandler
import coil.compose.SubcomposeAsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Bookmark
import compose.icons.feathericons.SkipBack
import compose.icons.feathericons.SkipForward
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Read(rootNavController: NavHostController, chapterId: String, chapterNumber: String, id: Int) {

    val context = LocalContext.current
    val cosco = rememberCoroutineScope()

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

    var currentChapter by rememberSaveable {
        mutableStateOf(chapterNumber)
    }

    var showUi by rememberSaveable {
        mutableStateOf(true)
    }

    val showSliderIndication by remember {
        mutableStateOf(false)
    }

    var pages by rememberSaveable {
        mutableStateOf<List<String?>>(listOf(null))
    }

    val interactionSource = remember { MutableInteractionSource() }

    var scale by rememberSaveable {
        mutableFloatStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val lazyListState = rememberLazyListState()
    var sliderPosition by remember {
        mutableFloatStateOf(0F)
    }
    var isScrollAnimating by remember {
        mutableStateOf(false)
    }

    val tresholdPercentage = 75

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }.distinctUntilChanged().collect { index ->
            if (!isScrollAnimating) sliderPosition = index+2.toFloat()
            val percent = (index / pages.size-1) * 100

            //might change later!
            if(percent >= tresholdPercentage) {
                val currentChapterSplit = chapterNumber.split(".")
                if(currentChapterSplit.size == 1)
                    MangaProgress().updateProgressWithId(context, id = id, progress = (currentChapter.toFloatOrNull() ?: 0f) + 1)
                else
                    MangaProgress().updateProgressWithId(context, id = id, progress = (currentChapter.toFloatOrNull() ?: 0f) + 0.5f)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (pages[0] == null) {
            val res = SourceHandler(MangaSources.MANGADEX).getPages(chapterId)
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
        BoxWithConstraints(
            modifier = Modifier
        ) {
            val state =
                rememberTransformableState { zoomChange, panChange, rotationChange ->
                    scale = (scale * zoomChange).coerceIn(1f, 5f)

                    val extraWidth = (scale - 1) * constraints.maxWidth
                    val extraHeight = (scale - 1) * constraints.maxHeight

                    val maxX = extraWidth / 2
                    val maxY = extraHeight / 2

                    offset = Offset(
                        x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                        y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                    )
                }
            LazyColumn(
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state)
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
                        )
                    }
                }
            }
        }

        //top bar and bottom box
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

        AnimatedVisibility(
            visible = showUi, enter = fadeIn(animationSpec = tween(400)),
            exit = fadeOut(animationSpec = tween(400))
        ) {
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.Black.copy(alpha = 0.8F))
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Slider(
                            value = sliderPosition,
                            onValueChange = {
                                sliderPosition = it
                                cosco.launch {
                                    isScrollAnimating = true
                                    lazyListState.animateScrollToItem(it.roundToInt())
                                    isScrollAnimating = false
                                }
                            },
                            valueRange = 0f..(if (pages.size - 3 > 0) pages.size - 3 else 1).toFloat(),
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(10.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                        .background(Color.White)
                                )
                            }
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {}) {
                                Icon(FeatherIcons.SkipBack, contentDescription = "prev chapter")
                            }
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(5.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(start = 15.dp, end = 15.dp)
                            ) {
                                Text(
                                    "${sliderPosition.roundToInt()} / ${pages.size}",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(onClick = {}) {
                                Icon(FeatherIcons.SkipForward, contentDescription = "next chapter")
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview
//fun ReadPreview() {
//    Read(
//        rootNavController = rememberNavController(),
//        chapterId = "79c644ac-2a30-43f7-a3fd-4b2ad7a4783c",
//        chapterNumber = "31"
//    )
//}