package com.example.planointeractivo_compose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planointeractivo_compose.data.Ambiente
import com.example.planointeractivo_compose.data.Marcador
import com.example.planointeractivo_compose.data.Plano
import com.example.planointeractivo_compose.data.PlanoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PlanoUiState(
    val cargando: Boolean = true,
    val plano: Plano? = null,
    val error: String? = null,
    val marcadorSeleccionado: Marcador? = null,
    val ambienteSeleccionado: Ambiente? = null
)

/**
 * Mantiene el estado del plano y sus marcadores (patron de SeleccionViewModel,
 * Laboratorio 05). Sobrevive a rotaciones y cambios de configuracion.
 */
class PlanoViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = PlanoRepository(application)

    private val _uiState = MutableStateFlow(PlanoUiState())
    val uiState: StateFlow<PlanoUiState> = _uiState.asStateFlow()

    init {
        cargarPlano()
    }

    private fun cargarPlano() {
        viewModelScope.launch {
            try {
                val plano = withContext(Dispatchers.IO) { repositorio.cargar() }
                _uiState.update { it.copy(cargando = false, plano = plano) }
            } catch (e: Exception) {
                _uiState.update { it.copy(cargando = false, error = "No se pudo cargar el plano: ${e.message}") }
            }
        }
    }

    fun seleccionarMarcador(marcador: Marcador) {
        val ambiente = _uiState.value.plano?.ambientes?.firstOrNull { it.id == marcador.ambienteId }
        _uiState.update { it.copy(marcadorSeleccionado = marcador, ambienteSeleccionado = ambiente) }
    }

    fun seleccionarAmbiente(ambiente: Ambiente?) {
        _uiState.update { it.copy(ambienteSeleccionado = ambiente) }
    }

    fun cerrarMarcador() {
        _uiState.update { it.copy(marcadorSeleccionado = null) }
    }
}
