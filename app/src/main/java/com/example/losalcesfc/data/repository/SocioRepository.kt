package com.example.losalcesfc.data.repository

import android.content.Context
import com.example.losalcesfc.data.dao.SocioDao
import com.example.losalcesfc.data.database.SocioDatabase
import com.example.losalcesfc.data.model.Socio
import kotlinx.coroutines.flow.Flow

class SocioRepository private constructor(
    private val dao: SocioDao
) {
    val socios: Flow<List<Socio>> = dao.obtenerSocios()

    suspend fun crear(socio: Socio) = dao.insertarSocio(socio)

    suspend fun actualizar(socio: Socio) = dao.actualizarSocio(socio)

    suspend fun eliminar(socio: Socio) = dao.eliminarSocio(socio)
    suspend fun obtenerPorId(id: Int): Socio? = dao.obtenerSocioPorId(id)

    suspend fun obtenerPorRut(rut: String): Socio? = dao.obtenerSocioPorRut(rut)

    suspend fun eliminarTodos() = dao.eliminarTodos()

    companion object {
        @Volatile
        private var INSTANCE: SocioRepository? = null

        fun getInstance(context: Context): SocioRepository {
            return INSTANCE ?: synchronized(this) {
                val db = SocioDatabase.getInstance(context.applicationContext)
                val repo = SocioRepository(db.socioDao())
                INSTANCE = repo
                repo
            }
        }

    }
}
