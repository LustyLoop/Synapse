package com.example.synapseapp



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.synapseapp.navigation.Routes
import com.example.synapseapp.ui.theme.MyAppTheme
import com.example.synapseapp.ui.theme.screens.GlobalBackground
import com.example.synapseapp.ui.theme.screens.MainScreen
import com.example.synapseapp.ui.theme.screens.SpeakScreen


class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.White.toArgb(),
                darkScrim = Color.Black.toArgb()
            )
        )


        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    GlobalBackground()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.MAIN_SCREEN
                    ) {
                        composable(route = Routes.MAIN_SCREEN) {
                            MainScreen(navController = navController)
                        }

                        composable(route = Routes.SPEAK_SCREEN) {
                            SpeakScreen(navController = navController)
                        }
                    }
                }
            }

        }
    }
}


