package com.example.tarjetascampus_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tarjetascampus_compose.morada.AppMorada
import com.example.tarjetascampus_compose.ui.theme.TarjetasCampus_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarjetasCampus_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppMorada()
                }
            }
        }
    }
}
