package com.example.planointeractivo_compose

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Variante morada: sans-serif, formas rectas, tarjetas planas con borde y
 * plano tipo "blueprint" oscuro con marcadores cuadrados numerados.
 * Sin titulo de marca.
 */
object Variante {
    const val titulo: String = ""

    // ---- Paleta
    val oscuro = Color(0xFF4A148C)
    val primario = Color(0xFF7B1FA2)
    val medio = Color(0xFFAB47BC)
    val claro = Color(0xFFF3E5F5)
    val pastel = Color(0xFFE1BEE7)
    val fondo = Color(0xFFFAF5FC)
    val acento = Color(0xFFFFC107)

    // ---- Estilo general
    val fuente: FontFamily = FontFamily.SansSerif
    val radioTarjeta: Dp = 6.dp
    val tarjetaConBorde = true
    val formaBoton: Shape = RoundedCornerShape(4.dp)
    val cabeceraPlana = true

    // ---- Plano interior: lienzo oscuro, marcadores cuadrados numerados
    val fondoPlano = Color(0xFF241038)
    val salon = Color(0xFF5E2B84)
    val patio = Color(0xFF2E6B57)
    val servicio = Color(0xFF3F4FA0)
    val colorEtiqueta = Color.White
    val colorPared = Color(0xFFD1C4E9)
    val colorPuerta = Color(0xFF241038)
    val colorEstanteria = Color(0xFFB39DDB)
    val marcador = Color(0xFFFFC107)
    val colorNumeroMarcador = Color(0xFF241038)
    val marcadorCuadrado = true
    val paredRedondeada = false
    val listaDeAmbientes = true

    // ---- Mapa del campus
    val fondoMapa = Color(0xFF241038)
    val edificio = Color(0xFF4A2A6B)
    val edificioActivo = Color(0xFFFFC107)
    val textoEdificio = Color.White
    val textoEdificioActivo = Color(0xFF241038)
    val edificioConBorde = true
}
