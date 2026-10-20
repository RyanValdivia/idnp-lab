package com.example.planointeractivo_compose.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planointeractivo_compose.Variante
import com.example.planointeractivo_compose.data.Ambiente
import com.example.planointeractivo_compose.data.Marcador
import com.example.planointeractivo_compose.data.Plano
import com.example.planointeractivo_compose.data.TipoAmbiente
import com.example.planointeractivo_compose.data.TipoLinea
import com.example.planointeractivo_compose.viewmodel.PlanoViewModel

@Composable
fun PlanoScreen(
    onBack: () -> Unit,
    viewModel: PlanoViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val plano = estado.plano

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Variante.fondo)
    ) {
        Cabecera(
            detalle = plano?.let { "${it.edificio} · ${it.piso}" } ?: "Plano interior",
            onBack = onBack
        )

        when {
            estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Variante.primario)
            }
            estado.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(estado.error!!, color = Variante.oscuro, modifier = Modifier.padding(24.dp))
            }
            plano != null -> Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Tarjeta(containerColor = Variante.fondoPlano) {
                    PlanoCanvas(
                        plano = plano,
                        ambienteSeleccionado = estado.ambienteSeleccionado,
                        marcadorSeleccionado = estado.marcadorSeleccionado?.offset,
                        onMarcadorClick = viewModel::seleccionarMarcador,
                        onAmbienteClick = viewModel::seleccionarAmbiente,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(plano.ancho / plano.alto)
                            .padding(8.dp)
                    )
                }
                Leyenda()
                if (Variante.listaDeAmbientes) {
                    ListaAmbientes(plano, onClick = viewModel::seleccionarMarcador)
                }
                InfoAmbiente(estado.ambienteSeleccionado)
                Text(
                    text = "Datos cargados desde plano_biblioteca.json · " +
                        "${plano.ambientes.size} ambientes · ${plano.marcadores.size} marcadores",
                    color = Variante.oscuro.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }

    estado.marcadorSeleccionado?.let { marcador ->
        AlertDialog(
            onDismissRequest = viewModel::cerrarMarcador,
            containerColor = Variante.fondo,
            shape = RoundedCornerShape(Variante.radioTarjeta),
            title = { Text(marcador.titulo, color = Variante.oscuro, fontWeight = FontWeight.Bold) },
            text = { Text(marcador.descripcion) },
            confirmButton = {
                TextButton(onClick = viewModel::cerrarMarcador) {
                    Text("Cerrar", color = Variante.primario)
                }
            }
        )
    }
}

@Composable
private fun PlanoCanvas(
    plano: Plano,
    ambienteSeleccionado: Ambiente?,
    marcadorSeleccionado: Offset?,
    onMarcadorClick: (Marcador) -> Unit,
    onAmbienteClick: (Ambiente?) -> Unit,
    modifier: Modifier = Modifier
) {
    val medidor = rememberTextMeasurer()
    val toqueMinimoPx = with(LocalDensity.current) { 28.dp.toPx() }

    Canvas(
        modifier = modifier.pointerInput(plano) {
            detectTapGestures { toque ->
                val escala = minOf(size.width / plano.ancho, size.height / plano.alto)
                val origen = Offset(
                    (size.width - plano.ancho * escala) / 2f,
                    (size.height - plano.alto * escala) / 2f
                )
                val p = (toque - origen) / escala
                val marcador = plano.marcadores.firstOrNull {
                    (p - it.offset).getDistance() <= maxOf(it.radio, toqueMinimoPx / escala)
                }
                if (marcador != null) {
                    onMarcadorClick(marcador)
                } else {
                    onAmbienteClick(plano.ambientes.firstOrNull { it.contiene(p) })
                }
            }
        }
    ) {
        val escala = minOf(size.width / plano.ancho, size.height / plano.alto)
        val origen = Offset(
            (size.width - plano.ancho * escala) / 2f,
            (size.height - plano.alto * escala) / 2f
        )
        fun px(o: Offset) = origen + o * escala

        // 1. Rectangulos de los ambientes
        plano.ambientes.forEach { a ->
            drawRect(
                color = colorDe(a.tipo),
                topLeft = px(Offset(a.x, a.y)),
                size = Size(a.w * escala, a.h * escala)
            )
        }
        // 2. Resalte del ambiente seleccionado
        ambienteSeleccionado?.let { a ->
            drawRect(
                color = Variante.acento.copy(alpha = 0.30f),
                topLeft = px(Offset(a.x, a.y)),
                size = Size(a.w * escala, a.h * escala)
            )
            drawRect(
                color = Variante.acento,
                topLeft = px(Offset(a.x, a.y)),
                size = Size(a.w * escala, a.h * escala),
                style = Stroke(width = 8f)
            )
        }
        // 3. Lineas: paredes, estanterias y puertas (en ese orden)
        listOf(TipoLinea.PARED, TipoLinea.ESTANTERIA, TipoLinea.PUERTA).forEach { tipo ->
            plano.lineas.filter { it.tipo == tipo }.forEach { l ->
                drawLine(
                    color = when (tipo) {
                        TipoLinea.PARED -> Variante.colorPared
                        TipoLinea.ESTANTERIA -> Variante.colorEstanteria
                        TipoLinea.PUERTA -> Variante.colorPuerta
                    },
                    start = px(l.inicio),
                    end = px(l.fin),
                    strokeWidth = when (tipo) {
                        TipoLinea.PARED -> if (Variante.paredRedondeada) 5f else 4f
                        TipoLinea.ESTANTERIA -> 7f
                        TipoLinea.PUERTA -> 9f
                    },
                    cap = if (Variante.paredRedondeada) StrokeCap.Round else StrokeCap.Butt
                )
            }
        }
        // 4. Etiquetas de cada ambiente
        plano.ambientes.forEach { a ->
            dibujarEtiqueta(medidor, a.nombre, px(Offset(a.x, a.y)) + Offset(10f, 8f), a.w * escala - 20f)
        }
        // 5. Marcadores
        plano.marcadores.forEachIndexed { i, m ->
            val centro = px(m.offset)
            val radio = m.radio * escala
            val seleccionado = marcadorSeleccionado == m.offset
            if (Variante.marcadorCuadrado) {
                // Variante morada: cuadrado redondeado con el numero del ambiente
                val lado = radio * 2f * (if (seleccionado) 1.25f else 1f)
                drawRoundRect(
                    color = Variante.marcador,
                    topLeft = centro - Offset(lado / 2f, lado / 2f),
                    size = Size(lado, lado),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                val numero = medidor.measure(
                    "${i + 1}",
                    TextStyle(
                        color = Variante.colorNumeroMarcador,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                drawText(
                    numero,
                    topLeft = centro - Offset(numero.size.width / 2f, numero.size.height / 2f)
                )
            } else {
                // Variante LibrarIA: circulo con punto blanco central
                if (seleccionado) {
                    drawCircle(Variante.marcador.copy(alpha = 0.25f), radius = radio * 2.1f, center = centro)
                }
                drawCircle(Variante.marcador, radius = radio, center = centro)
                drawCircle(Color.White, radius = radio * 0.38f, center = centro)
            }
        }
    }
}

private fun colorDe(tipo: TipoAmbiente) = when (tipo) {
    TipoAmbiente.SALON -> Variante.salon
    TipoAmbiente.PATIO -> Variante.patio
    TipoAmbiente.SERVICIO -> Variante.servicio
}

private fun DrawScope.dibujarEtiqueta(medidor: TextMeasurer, texto: String, posicion: Offset, anchoMax: Float) {
    drawText(
        textMeasurer = medidor,
        text = texto,
        topLeft = posicion,
        style = TextStyle(color = Variante.colorEtiqueta, fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
        size = Size(anchoMax.coerceAtLeast(1f), 400f)
    )
}

@Composable
private fun Leyenda() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemLeyenda(Variante.salon, "Salón", RoundedCornerShape(3.dp))
        ItemLeyenda(Variante.patio, "Patio", RoundedCornerShape(3.dp))
        ItemLeyenda(Variante.servicio, "Servicio", RoundedCornerShape(3.dp))
        ItemLeyenda(Variante.marcador, "Marcador", if (Variante.marcadorCuadrado) RoundedCornerShape(3.dp) else CircleShape)
    }
}

@Composable
private fun ItemLeyenda(color: Color, texto: String, forma: androidx.compose.ui.graphics.Shape) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(14.dp)
                .clip(forma)
                .background(color)
        )
        Spacer(Modifier.width(5.dp))
        Text(texto, fontSize = 12.sp)
    }
}

@Composable
private fun InfoAmbiente(ambiente: Ambiente?) {
    Tarjeta(containerColor = Variante.claro, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
            if (ambiente == null) {
                Text("Toca un ambiente o un marcador del plano.", color = Variante.oscuro, fontSize = 14.sp)
            } else {
                Text(ambiente.nombre, color = Variante.oscuro, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    "Tipo: ${nombreTipo(ambiente.tipo)} · " +
                        "${ambiente.w.toInt()} × ${ambiente.h.toInt()} u",
                    color = Variante.primario,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun nombreTipo(tipo: TipoAmbiente) = when (tipo) {
    TipoAmbiente.SALON -> "Salón"
    TipoAmbiente.PATIO -> "Patio"
    TipoAmbiente.SERVICIO -> "Servicio"
}

/** Lista numerada de ambientes (variante morada): cada fila abre el detalle del marcador. */
@Composable
private fun ListaAmbientes(plano: Plano, onClick: (Marcador) -> Unit) {
    Tarjeta(containerColor = Color.White, modifier = Modifier.fillMaxWidth()) {
        plano.marcadores.forEachIndexed { i, m ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(m) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Variante.marcador)
                ) {
                    Text(
                        "${i + 1}",
                        color = Variante.colorNumeroMarcador,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(m.titulo, color = Variante.oscuro, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }
        }
    }
}
