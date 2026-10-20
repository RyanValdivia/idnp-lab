package com.example.planointeractivo_compose.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planointeractivo_compose.Variante

private data class EdificioMapa(
    val nombre: String,
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
    val tienePlano: Boolean = false
)

private val edificios = listOf(
    EdificioMapa("Biblioteca Central", 40f, 40f, 300f, 180f, tienePlano = true),
    EdificioMapa("Pabellón A", 380f, 40f, 240f, 110f),
    EdificioMapa("Pabellón B", 660f, 40f, 300f, 110f),
    EdificioMapa("Auditorio", 380f, 190f, 240f, 150f),
    EdificioMapa("Comedor", 40f, 260f, 300f, 80f),
    EdificioMapa("Laboratorios", 660f, 190f, 300f, 150f)
)

/** Pantalla "Mapa" (Laboratorio 05): punto de entrada al plano interior. */
@Composable
fun MapaScreen(onVerPlano: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Variante.fondo)
    ) {
        Cabecera(detalle = "Mapa del campus · toca la biblioteca")

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Tarjeta(containerColor = Variante.fondoMapa) {
                CampusCanvas(
                    onBibliotecaClick = onVerPlano,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1000f / 380f)
                )
            }

            Tarjeta(
                containerColor = Variante.claro,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVerPlano() }
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Biblioteca Central",
                        color = Variante.oscuro,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Planta baja con 9 ambientes: salones, patios y servicios.",
                        color = Variante.primario,
                        fontSize = 14.sp
                    )
                    Button(
                        onClick = onVerPlano,
                        shape = Variante.formaBoton,
                        colors = ButtonDefaults.buttonColors(containerColor = Variante.primario),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Ver plano interior")
                    }
                }
            }
        }
    }
}

@Composable
private fun CampusCanvas(onBibliotecaClick: () -> Unit, modifier: Modifier = Modifier) {
    val medidor = rememberTextMeasurer()

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { toque ->
                val escala = minOf(size.width / 1000f, size.height / 380f)
                val p = toque / escala
                val b = edificios.first { it.tienePlano }
                if (p.x in b.x..(b.x + b.w) && p.y in b.y..(b.y + b.h)) onBibliotecaClick()
            }
        }
    ) {
        val escala = minOf(size.width / 1000f, size.height / 380f)
        edificios.forEach { e ->
            val color = if (e.tienePlano) Variante.edificioActivo else Variante.edificio
            val radioEsquina = if (Variante.edificioConBorde) 4f else 14f
            drawRoundRect(
                color = color,
                topLeft = Offset(e.x, e.y) * escala,
                size = Size(e.w, e.h) * escala,
                cornerRadius = CornerRadius(radioEsquina, radioEsquina)
            )
            if (Variante.edificioConBorde) {
                drawRoundRect(
                    color = Variante.medio,
                    topLeft = Offset(e.x, e.y) * escala,
                    size = Size(e.w, e.h) * escala,
                    cornerRadius = CornerRadius(radioEsquina, radioEsquina),
                    style = Stroke(width = 3f)
                )
            }
            drawText(
                textMeasurer = medidor,
                text = e.nombre,
                topLeft = Offset(e.x, e.y) * escala + Offset(12f, 10f),
                style = TextStyle(
                    color = if (e.tienePlano) Variante.textoEdificioActivo else Variante.textoEdificio,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                size = Size((e.w * escala - 24f).coerceAtLeast(1f), 400f)
            )
        }
    }
}
