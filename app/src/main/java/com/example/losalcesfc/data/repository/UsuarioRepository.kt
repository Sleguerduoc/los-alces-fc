package com.example.losalcesfc.data.repository

import android.content.Context
import com.example.losalcesfc.data.database.UsuarioDatabase
import com.example.losalcesfc.data.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository private constructor(context: Context) {

    private val dao = UsuarioDatabase.get(context).usuarioDao()


    fun obtenerTodos(): Flow<List<Usuario>> = dao.obtenerTodos()
    suspend fun obtenerPorId(id: Int) = dao.obtenerPorId(id)
    suspend fun guardar(usuario: Usuario) = dao.insertar(usuario)
    suspend fun actualizar(usuario: Usuario) = dao.actualizar(usuario)
    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)


    suspend fun validarLoginTextoPlano(emailOrRut: String, passwordPlain: String): Usuario? {
        val ident = emailOrRut.trim()

        // Buscar por email o RUT (varias formas)
        var user = dao.obtenerPorEmail(ident)
        if (user == null) user = dao.obtenerPorRutRaw(ident)
        if (user == null) {
            val canon = ident.replace(".", "").replace("-", "").lowercase()
            user = dao.obtenerPorRutCanon(canon)
        }

        if (user == null) return null
        if (!user.activo) return null

        return if (user.password == passwordPlain) user else null
    }

    suspend fun seedAdminIfMissing() {
        val admin = dao.obtenerPorEmail("admin@alces.cl")
        if (admin == null) {
            val usuarioAdmin = Usuario(
                nombre = "Administrador General",
                email = "admin@alces.cl",
                rut = "11.111.111-1",
                rol = "Admin",
                activo = true,
                fotoPath = null,
                password = "admin123"
            )
            dao.insertar(usuarioAdmin)
        }
    }

    companion object {
        @Volatile private var INSTANCE: UsuarioRepository? = null
        fun instance(context: Context): UsuarioRepository =
            INSTANCE ?: synchronized(this) {
                UsuarioRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}

