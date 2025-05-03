package com.example.pov_clone_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.pov_clone_project.Database.ThemePreference
import com.example.pov_clone_project.ui.theme.POV_Clone_projectTheme
import com.example.pov_clone_project.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            POV_Clone_projectTheme() { // your theme (auto created if you made project)
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MediCartApp()
                }
            }
        }
    }

}

@Composable
fun MediCartApp() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}
