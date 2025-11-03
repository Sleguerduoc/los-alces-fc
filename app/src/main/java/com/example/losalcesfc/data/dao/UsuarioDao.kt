package com.example.losalcesfc.data.dao

import androidx.room.*
import com.example.losalcesfc.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE LOWER(email) = LOWER(:ident) LIMIT 1")
    suspend fun obtenerPorEmail(ident: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE rut = :rut LIMIT 1")
    suspend fun obtenerPorRutRaw(rut: String): Usuario?

    @Query("""
        SELECT * FROM usuarios
        WHERE REPLACE(REPLACE(UPPER(rut),'.',''),'-','') = :canonRut
        LIMIT 1
    """)
    suspend fun obtenerPorRutCanon(canonRut: String): Usuario?
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerPorEmailExact(email: String): Usuario?

}

