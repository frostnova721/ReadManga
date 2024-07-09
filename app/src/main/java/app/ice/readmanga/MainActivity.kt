package app.ice.readmanga

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.ui.navigator.ReadMangaNavGraph
import app.ice.readmanga.ui.pages.MainScreen
import app.ice.readmanga.ui.theme.ReadMangaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        checkAndRequestPermissions(this)

        super.onCreate(savedInstanceState)
        setContent {
            ReadMangaTheme {
                // A surface container using the 'background' color from the theme
                val rootNavController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReadMangaNavGraph(navController = rootNavController)
                }
            }
        }
    }
}



fun checkAndRequestPermissions(context: Context) {

}
