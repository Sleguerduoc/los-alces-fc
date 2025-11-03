package com.example.losalcesfc.data.dao

import androidx.room.*
import com.example.losalcesfc.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE rut = :rut LIMIT 1")
    suspend fun obtenerPorRutRaw(rut: String): Usuario?

    @Query("""
        SELECT * FROM usuarios 
        WHERE REPLACE(REPLACE(LOWER(rut),'.',''),'-','') = :rutCanon 
        LIMIT 1
    """)
    suspend fun obtenerPorRutCanon(rutCanon: String): Usuario?

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)
}


