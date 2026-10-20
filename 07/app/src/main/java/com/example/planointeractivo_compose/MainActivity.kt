package com.example.planointeractivo_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planointeractivo_compose.screen.MapaScreen
import com.example.planointeractivo_compose.screen.PlanoScreen
import com.example.planointeractivo_compose.ui.theme.PlanoInteractivo_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanoInteractivo_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mapa") {
                        composable("mapa") {
                            MapaScreen(onVerPlano = { navController.navigate("plano") })
                        }
                        composable("plano") {
                            PlanoScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
