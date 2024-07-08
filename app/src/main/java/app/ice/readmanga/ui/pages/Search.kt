package app.ice.readmanga.ui.pages

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.ice.readmanga.core.database.anilist.Anilist
import app.ice.readmanga.types.AnilistSearchResult
import app.ice.readmanga.ui.models.Cards
import compose.icons.FeatherIcons
import compose.icons.feathericons.Filter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {

    var searchResults by remember { mutableStateOf<List<AnilistSearchResult>>(emptyList()) }

    var showFilterSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var isSearching by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Column {
        Surface(modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 20.dp)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                singleLine = true,
                label = {
                    Text("search")
                },
                shape = RoundedCornerShape(30),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (searchText.text.length > 1) {
                            coroutineScope.launch {
                                isSearching = true
                                val sr = Anilist().search(searchText.text)
                                searchResults = sr
                                isSearching = false
                            }
                        }
                    }
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Results",
                fontWeight = FontWeight.Bold,
            )
            TextButton(onClick = {
                showFilterSheet = true
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(FeatherIcons.Filter, contentDescription = "filter")
                    Text(text = "filter")
                }
            }
        }
        if (isSearching)
            CircularProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
        else
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                items(searchResults.size) { index ->
                    Cards(navController = navHostController).mangaCard(
                        id = searchResults[index].id,
                        title = searchResults[index].title.english
                            ?: searchResults[index].title.romaji ?: "",
                        imageUrl = searchResults[index].cover,
                        averageScore = searchResults[index].rating,
                    )
                }
            }
        BottomSheet(isBottomSheetVisible = showFilterSheet, sheetState = sheetState, onDismiss = {
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion { showFilterSheet = false }
        })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {

    if (isBottomSheetVisible) {

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(20.dp),
            dragHandle = {
                BottomSheetDefaults.DragHandle()
            },
            scrimColor = Color.Black.copy(alpha = .5f),
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {

            }

        }

    }

}
