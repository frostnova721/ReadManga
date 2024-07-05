package app.ice.readmanga.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.ui.BottomNavigationBarGraph
import app.ice.readmanga.ui.models.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController();

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row {
                    Text(text = "ReadManga")
                }
            })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                BottomNavigationBarGraph(navController = navController)
            }

        }
    )
}