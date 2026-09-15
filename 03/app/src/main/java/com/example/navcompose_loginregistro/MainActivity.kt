package com.example.navcompose_loginregistro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navcompose_loginregistro.screens.HomeScreen
import com.example.navcompose_loginregistro.screens.LoginScreen
import com.example.navcompose_loginregistro.screens.RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController,
                startDestination = "login"
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginExitoso = {
                            usuario ->
                            navController.navigate("home/$usuario")
                        },

                        onIrARegistro = {
                            navController.navigate("registro")
                        }
                    )
                }
                composable("registro") {
                    RegistroScreen(
                        onRegistroExitoso = {
                            navController.popBackStack()
                        },
                        onCancelar = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    "home/{usuario}",
                    arguments = listOf(navArgument("usuario") { type = NavType.StringType })
                ) { backStackEntry ->
                    val usuario = backStackEntry.arguments?.getString("usuario") ?: ""
                    HomeScreen(usuario)
                }
            }
        }
    }
}