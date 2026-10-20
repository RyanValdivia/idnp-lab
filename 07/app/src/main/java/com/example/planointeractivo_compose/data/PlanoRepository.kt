package com.example.planointeractivo_compose.data

import android.content.Context
import androidx.compose.ui.geometry.Offset
import org.json.JSONObject
import java.io.File

/**
 * Persistencia del plano en un archivo JSON, igual que el FileManager del
 * Laboratorio 03: el archivo vive en el almacenamiento interno de la app
 * (filesDir). La primera vez se siembra copiando assets/plano_biblioteca.json.
 */
class PlanoRepository(private val context: Context) {
    private val nombreArchivo = "plano_biblioteca.json"

    private fun archivo(): File {
        val destino = File(context.filesDir, nombreArchivo)
        if (!destino.exists()) {
            context.assets.open(nombreArchivo).use { entrada ->
                destino.outputStream().use { salida -> entrada.copyTo(salida) }
            }
        }
        return destino
    }

    fun cargar(): Plano = parsear(archivo().readText())

    private fun parsear(texto: String): Plano {
        val raiz = JSONObject(texto)

        val ambientes = raiz.getJSONArray("ambientes").let { arr ->
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                Ambiente(
                    id = o.getString("id"),
                    nombre = o.getString("nombre"),
                    tipo = TipoAmbiente.valueOf(o.getString("tipo").uppercase()),
                    x = o.getDouble("x").toFloat(),
                    y = o.getDouble("y").toFloat(),
                    w = o.getDouble("w").toFloat(),
                    h = o.getDouble("h").toFloat()
                )
            }
        }
        val lineas = raiz.getJSONArray("lineas").let { arr ->
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                Linea(
                    tipo = TipoLinea.valueOf(o.getString("tipo").uppercase()),
                    inicio = Offset(o.getDouble("x1").toFloat(), o.getDouble("y1").toFloat()),
                    fin = Offset(o.getDouble("x2").toFloat(), o.getDouble("y2").toFloat())
                )
            }
        }
        val marcadores = raiz.getJSONArray("marcadores").let { arr ->
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                Marcador(
                    ambienteId = o.getString("ambienteId"),
                    offset = Offset(o.getDouble("x").toFloat(), o.getDouble("y").toFloat()),
                    radio = o.getDouble("radio").toFloat(),
                    titulo = o.getString("titulo"),
                    descripcion = o.getString("descripcion")
                )
            }
        }
        return Plano(
            edificio = raiz.getString("edificio"),
            piso = raiz.getString("piso"),
            ancho = raiz.getDouble("ancho").toFloat(),
            alto = raiz.getDouble("alto").toFloat(),
            ambientes = ambientes,
            lineas = lineas,
            marcadores = marcadores
        )
    }
}
