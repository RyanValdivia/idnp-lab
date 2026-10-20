package com.example.planointeractivo_compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.planointeractivo_compose.Variante

// El esquema se construye con la paleta de la variante (libraria / morada).
private val EsquemaVariante = lightColorScheme(
    primary = Variante.primario,
    onPrimary = Color.White,
    primaryContainer = Variante.pastel,
    onPrimaryContainer = Variante.oscuro,
    secondary = Variante.medio,
    secondaryContainer = Variante.claro,
    onSecondaryContainer = Variante.oscuro,
    background = Variante.fondo,
    surface = Variante.fondo,
    surfaceVariant = Variante.claro,
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun PlanoInteractivo_ComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaVariante,
        typography = Typography,
        content = content
    )
}
