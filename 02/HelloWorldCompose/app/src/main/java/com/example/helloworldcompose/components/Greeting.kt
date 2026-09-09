package com.example.helloworldcompose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.example.helloworldcompose.ui.theme.HelloWorldComposeTheme

/**
 * Ejercicio base resuelto en clase: proyecto Compose "Hello World".
 * Se conserva como referencia del primer ejercicio antes de construir
 * la app de registro de lectura (RegistroLibroScreen).
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldComposeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Greeting("Android")
        }
    }
}
