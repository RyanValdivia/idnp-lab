package com.example.planointeractivo_compose.data

import androidx.compose.ui.geometry.Offset

enum class TipoAmbiente { SALON, PATIO, SERVICIO }

enum class TipoLinea { PARED, PUERTA, ESTANTERIA }

/** Rectangulo del plano (coordenadas en unidades del plano, no en pixeles). */
data class Ambiente(
    val id: String,
    val nombre: String,
    val tipo: TipoAmbiente,
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float
) {
    fun contiene(p: Offset) = p.x in x..(x + w) && p.y in y..(y + h)
}

data class Linea(
    val tipo: TipoLinea,
    val inicio: Offset,
    val fin: Offset
)

data class Marcador(
    val ambienteId: String,
    val offset: Offset,
    val radio: Float,
    val titulo: String,
    val descripcion: String
)

data class Plano(
    val edificio: String,
    val piso: String,
    val ancho: Float,
    val alto: Float,
    val ambientes: List<Ambiente>,
    val lineas: List<Linea>,
    val marcadores: List<Marcador>
)
