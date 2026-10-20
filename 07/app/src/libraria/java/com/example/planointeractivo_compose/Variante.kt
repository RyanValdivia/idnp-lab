package com.example.planointeractivo_compose

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Variante "LibrarIA": naranja, serif, formas muy redondeadas y plano claro.
 * Titulo de marca visible.
 */
object Variante {
    const val titulo: String = "LibrarIA"

    // ---- Paleta
    val oscuro = Color(0xFFBF360C)
    val primario = Color(0xFFE65100)
    val medio = Color(0xFFFB8C00)
    val claro = Color(0xFFFFF3E0)
    val pastel = Color(0xFFFFCC80)
    val fondo = Color(0xFFFFF8F1)
    val acento = Color(0xFFFB8C00)

    // ---- Estilo general
    val fuente: FontFamily = FontFamily.Serif
    val radioTarjeta: Dp = 28.dp
    val tarjetaConBorde = false
    val formaBoton: Shape = CircleShape
    val cabeceraPlana = false

    // ---- Plano interior: lienzo claro, marcadores circulares
    val fondoPlano = Color.White
    val salon = Color(0xFFFFE0B2)
    val patio = Color(0xFFDCEDC8)
    val servicio = Color(0xFFFFF59D)
    val colorEtiqueta = Color(0xFF263238)
    val colorPared = Color(0xFF37474F)
    val colorPuerta = Color.White
    val colorEstanteria = Color(0xFF8D6E63)
    val marcador = Color(0xFFD84315)
    val colorNumeroMarcador = Color.White
    val marcadorCuadrado = false
    val paredRedondeada = true
    val listaDeAmbientes = false

    // ---- Mapa del campus
    val fondoMapa = Color(0xFFE8F5E9)
    val edificio = Color(0xFFFFCC80)
    val edificioActivo = Color(0xFFE65100)
    val textoEdificio = Color(0xFFBF360C)
    val textoEdificioActivo = Color.White
    val edificioConBorde = false
}
