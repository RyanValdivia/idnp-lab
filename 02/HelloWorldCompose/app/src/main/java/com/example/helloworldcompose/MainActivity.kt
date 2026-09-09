package com.example.helloworldcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.helloworldcompose.components.FileManager
import com.example.helloworldcompose.components.RegistroLibroScreen
import com.example.helloworldcompose.ui.theme.HelloWorldComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val fileManager = FileManager(this)

        setContent {
            HelloWorldComposeTheme(dynamicColor = false) {
                RegistroLibroScreen(
                    fileManager = fileManager
                )
            }
        }
    }
}
