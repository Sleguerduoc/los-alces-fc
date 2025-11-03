package com.example.losalcesfc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.losalcesfc.data.model.Socio
import kotlinx.coroutines.flow.Flow

@Dao
interface SocioDao {

    // --- Insertar un nuevo socio ---
    @Insert
    suspend fun insertarSocio(socio: Socio)

    // --- Obtener todos los socios ---
    @Query("SELECT * FROM socios ORDER BY nombre ASC")
    fun obtenerSocios(): Flow<List<Socio>>

    // --- Buscar socio por RUT ---
    @Query("SELECT * FROM socios WHERE rut = :rut LIMIT 1")
    suspend fun obtenerSocioPorRut(rut: String): Socio?

    @Query("SELECT * FROM socios WHERE id = :id LIMIT 1")
    suspend fun obtenerSocioPorId(id: Int): Socio?

    // --- Actualizar datos de un socio ---
    @Update
    suspend fun actualizarSocio(socio: Socio)

    // --- Eliminar socio (por objeto) ---
    @Delete
    suspend fun eliminarSocio(socio: Socio)

    // --- Eliminar todos los socios ---
    @Query("DELETE FROM socios")
    suspend fun eliminarTodos()
}
