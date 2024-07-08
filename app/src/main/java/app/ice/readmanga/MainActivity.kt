package app.ice.readmanga

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.ice.readmanga.ui.RootNavigationgraph
import app.ice.readmanga.ui.pages.Home
import app.ice.readmanga.ui.pages.MainScreen
import app.ice.readmanga.ui.theme.ReadMangaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        checkAndRequestPermissions(this)

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ReadMangaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigationgraph(navController = navController)
//                MainScreen()
                }
            }
        }
    }
}



fun checkAndRequestPermissions(context: Context) {

}
