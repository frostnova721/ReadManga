package app.ice.readmanga.ui.pages.info

import android.widget.GridView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ReadSection() {
    val list = List(15) { index ->
        index
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {

            }
            LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
                items(list.size) {item ->
                }
            }
        }
    }
}

@Composable
fun chapterItem() {
    
}