
package com.example.losalcesfc.data.repository

import android.content.Context
import com.example.losalcesfc.data.database.UsuarioDatabase
import com.example.losalcesfc.data.model.Usuario
import com.example.losalcesfc.utils.RutUtil   // usa tu util existente (o cambia a .util si corresponde)
import kotlinx.coroutines.flow.Flow

class UsuarioRepository private constructor(context: Context) {
    private val dao = UsuarioDatabase.get(context).usuarioDao()

    fun obtenerTodos(): Flow<List<Usuario>> = dao.obtenerTodos()
    suspend fun obtenerPorId(id: Int) = dao.obtenerPorId(id)
    suspend fun guardar(usuario: Usuario) = dao.insertar(usuario)
    suspend fun actualizar(usuario: Usuario) = dao.actualizar(usuario)
    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)

    /** Crea admin si no existe (password en TEXTO PLANO). */
    suspend fun seedAdminIfMissing() {
        // Si tu DAO no tiene obtenerPorEmailExact, usa obtenerPorEmail(...)
        val admin = dao.obtenerPorEmailExact("admin@alces.cl")
            ?: dao.obtenerPorEmail("admin@alces.cl")
        if (admin == null) {
            val u = Usuario(
                nombre   = "Administrador General",
                email    = "admin@alces.cl",
                rut      = "11.111.111-1",
                rol      = "Admin",
                activo   = true,
                fotoPath = null,
                // 👇 TEXTO PLANO
                password = "admin123"
            )
            dao.insertar(u)
        }
    }

    /** Login comparando password en TEXTO PLANO. */
    suspend fun validarLogin(emailOrRut: String, passwordPlain: String): Usuario? {
        val ident = emailOrRut.trim()

        // Buscar por email o RUT (crudo/canónico)
        var user = dao.obtenerPorEmail(ident)
        if (user == null) user = dao.obtenerPorRutRaw(ident)
        if (user == null) user = dao.obtenerPorRutCanon(RutUtil.canon(ident))

        if (user != null && user.activo) {
            // 👇 Comparación directa (no hash)
            if (user.password == passwordPlain) return user
        }
        return null
    }

    companion object {
        @Volatile private var INSTANCE: UsuarioRepository? = null
        fun instance(context: Context): UsuarioRepository =
            INSTANCE ?: synchronized(this) {
                UsuarioRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}

