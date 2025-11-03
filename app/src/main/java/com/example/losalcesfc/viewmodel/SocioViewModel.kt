package com.example.losalcesfc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.losalcesfc.data.model.Socio
import com.example.losalcesfc.data.repository.SocioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SocioViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = SocioRepository.getInstance(app)

    val socios: StateFlow<List<Socio>> =
        repo.socios.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun guardar(s: Socio) = viewModelScope.launch {
        runCatching { repo.crear(s) }
            .onSuccess { _mensaje.value = "Socio agregado" }
            .onFailure { _mensaje.value = it.message }
    }
    fun actualizar(s: Socio) = viewModelScope.launch {
        runCatching { repo.actualizar(s) }
            .onSuccess { _mensaje.value = "Socio actualizado" }
            .onFailure { _mensaje.value = it.message }
    }
    fun eliminar(s: Socio) = viewModelScope.launch {
        runCatching { repo.eliminar(s) }
            .onSuccess { _mensaje.value = "Socio eliminado" }
            .onFailure { _mensaje.value = it.message }
    }
    fun limpiarMensaje() { _mensaje.value = null }
    suspend fun obtenerPorId(id: Int): Socio? = repo.obtenerPorId(id)
}
