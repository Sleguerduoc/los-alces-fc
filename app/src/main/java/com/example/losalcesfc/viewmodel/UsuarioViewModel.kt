package com.example.losalcesfc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.losalcesfc.data.model.Usuario
import com.example.losalcesfc.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UsuarioRepository.instance(app)

    val usuarios = repo.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    suspend fun obtenerPorId(id: Int) = repo.obtenerPorId(id)

    fun guardar(usuario: Usuario) = viewModelScope.launch { repo.guardar(usuario) }
    fun actualizar(usuario: Usuario) = viewModelScope.launch { repo.actualizar(usuario) }
    fun eliminar(usuario: Usuario) = viewModelScope.launch { repo.eliminar(usuario) }
}
