package com.example.helloworldcompose.components

import android.content.Context

/**
 * Encapsula la lectura/escritura del registro de lectura en el
 * almacenamiento interno de la app (openFileOutput/openFileInput,
 * MODE_PRIVATE) como archivo de texto plano.
 */
class FileManager(private val context: Context) {
    private val nombreArchivo = "libro.txt"

    fun guardarRegistro(
        titulo: String,
        autor: String,
        paginas: String
    ) {
        val registro = """
            Título: $titulo
            Autor: $autor
            Páginas leídas: $paginas
        """.trimIndent()

        context.openFileOutput(
            nombreArchivo,
            Context.MODE_PRIVATE
        ).use { archivo ->
            archivo.write(registro.toByteArray())
        }
    }

    fun leerRegistro(): String {
        return try {
            context.openFileInput(nombreArchivo)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: Exception) {
            "No existe ningún registro guardado."
        }
    }
}
