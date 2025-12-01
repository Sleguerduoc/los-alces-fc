package com.example.losalcesfc.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "socios")
data class Socio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val rut: String,
    val email: String,
    val telefono: String?,
    val plan: String,
    val activo: Boolean,
    val fotoPath: String? = null,
    val domicilio: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)
